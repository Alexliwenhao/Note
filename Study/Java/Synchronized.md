ObjectMonitor结构
```
ObjectMonitor() {

    _header = NULL; //mark word

    _count = 0;

    _waiters = 0; //等待线程数

    _recursions = 0; // 递归；线程的重入次数，典型的System.out.println

    _object = NULL; //  对应synchronized (object)对应里面的object

    _owner = NULL; // 标识拥有该monitor的线程

    _WaitSet = NULL; // 因为调用object.wait()方法而被阻塞的线程会被放在该队列中

    _WaitSetLock = 0 ;

    _Responsible = NULL;

    _succ = NULL;

    _cxq = NULL; // 竞争队列，所有请求锁的线程首先会被放在这个队列中

    FreeNext = NULL;

    _EntryList = NULL; // 阻塞；第二轮竞争锁仍然没有抢到的线程（在exit之后扔没有竞争到的线程将有可能会被同步至此）

    _SpinFreq = 0;

    _SpinClock = 0;

    OwnerIsThread = 0;

    _previous_owner_tid = 0;

  }
```

```
//  64 bits:

//  --------

//  unused:25 hash:31 -->| unused:1   age:4    biased_lock:1 lock:2 (normal object)

//  JavaThread*:54 epoch:2 unused:1   age:4    biased_lock:1 lock:2 (biased object)

//  PromotedObject*:61 --------------------->| promo_bits:3 ----->| (CMS promoted object)

//  size:64 ----------------------------------------------------->| (CMS free block)

//

//  unused:25 hash:31 -->| cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && normal object)

//  JavaThread*:54 epoch:2 cms_free:1 age:4    biased_lock:1 lock:2 (COOPs && biased object)

//  narrowOop:32 unused:24 cms_free:1 unused:4 promo_bits:3 ----->| (COOPs && CMS promoted object)

//  unused:21 size:35 -->| cms_free:1 unused:7 ------------------>| (COOPs && CMS free block)
```
## 为什么偏向锁要放在无锁之前
在Java虚拟机中，为了提高synchronized关键字的性能，引入了偏向锁（Biased Locking）机制。偏向锁是一种针对单线程访问同步块的优化机制，它通过记录锁对象的标识符和线程的标识符来避免重复获取锁的开销。

在Java虚拟机中，偏向锁会被优先使用，因为偏向锁的获取和释放都比较快，不需要涉及到多个线程之间的竞争。如果偏向锁无法使用，则会尝试使用轻量级锁和重量级锁。轻量级锁和重量级锁的获取和释放都需要涉及到多个线程之间的竞争，因此开销比较大。

偏向锁要放在无锁之前的原因是，偏向锁的获取和释放都比较快，不需要涉及到多个线程之间的竞争，因此比无锁更快。如果将偏向锁放在无锁之后，则会增加无锁的使用次数，从而降低程序的性能。

另外，偏向锁的使用是基于锁对象的状态，如果锁对象已经被多个线程访问，则无法使用偏向锁。因此，将偏向锁放在无锁之前可以优先使用偏向锁，从而避免多线程访问同步块的开销。

需要注意的是，偏向锁的使用场景是单线程访问同步块的情况，如果多个线程访问同步块，则偏向锁会失效，需要使用轻量级锁或重量级锁来保证线程安全。

## synchronized底层原理中为啥设计fast_enter和slow_enter
在Java虚拟机中，synchronized关键字的底层实现是基于监视器（Monitor）对象的。当一个线程访问一个被synchronized关键字修饰的方法或代码块时，它会尝试获取该方法或代码块所关联的监视器对象的锁。如果锁没有被其他线程占用，则该线程可以直接获取锁并进入临界区；否则，该线程就会进入阻塞状态，等待锁的释放。

在Java虚拟机中，为了提高synchronized关键字的性能，引入了fast_enter和slow_enter两种获取锁的方式。

fast_enter是一种快速获取锁的方式，它使用CAS（Compare and Swap）指令来尝试获取锁。如果CAS操作成功，则该线程可以直接获取锁并进入临界区；否则，该线程就会调用slow_enter方法，使用传统的方式获取锁。

slow_enter是一种传统的获取锁的方式，它会先尝试使用轻量级锁（Lightweight Locking）来获取锁，如果轻量级锁获取失败，则会升级为重量级锁（Heavyweight Locking）来获取锁。轻量级锁和重量级锁的区别在于，轻量级锁使用CAS指令来获取锁，而重量级锁则使用操作系统提供的同步机制来获取锁。

fast_enter和slow_enter的设计是为了提高synchronized关键字的性能。在多线程环境下，使用fast_enter可以避免线程的阻塞，从而提高程序的并发性能。但是，fast_enter的实现依赖于CAS指令的性能和可用性，如果CAS指令的性能较差或者CAS指令不可用，则会影响synchronized关键字的性能。因此，Java虚拟机会根据具体的情况选择fast_enter和slow_enter两种方式来获取锁。

## 示例代码：

```
public class SynchronizedTest {  
  
    private static Object lock = new Object();  
  
    public static void main(String[] args) {  
        int num = 0;  
        synchronized (lock) {  
            num++;  
            System.out.println("num = " + num);  
        }  
  
    }  
}
```

得到字节码：
```
Compiled from "SynchronizedTest.java"
public class com.alex.SynchronizedTest {
  public com.alex.SynchronizedTest();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: iconst_0
       1: istore_1
       2: getstatic     #2                  // Field lock:Ljava/lang/Object;
       5: dup
       6: astore_2
       7: monitorenter
       8: iinc          1, 1
      11: getstatic     #3                  // Field java/lang/System.out:Ljava/io/PrintStream;
      14: new           #4                  // class java/lang/StringBuilder
      17: dup
      18: invokespecial #5                  // Method java/lang/StringBuilder."<init>":()V
      21: ldc           #6                  // String num =
      23: invokevirtual #7                  // Method java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
      26: iload_1
      27: invokevirtual #8                  // Method java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
      30: invokevirtual #9                  // Method java/lang/StringBuilder.toString:()Ljava/lang/String;
      33: invokevirtual #10                 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      36: aload_2
      37: monitorexit
      38: goto          46
      41: astore_3
      42: aload_2
      43: monitorexit
      44: aload_3
      45: athrow
      46: return
    Exception table:
       from    to  target type
           8    38    41   any
          41    44    41   any

  static {};
    Code:
       0: new           #11                 // class java/lang/Object
       3: dup
       4: invokespecial #1                  // Method java/lang/Object."<init>":()V
       7: putstatic     #2                  // Field lock:Ljava/lang/Object;
      10: return
}
```

发现其中Synchronized依赖于monitorenter和monitorexit来进行加锁解锁
基于monitorenter为入口，沿着无锁态->偏向锁->轻量级锁->重量级锁的路径来分析synchronized的实现过程：
## monitorenter对应hotspot源码内容
```
IRT_ENTRY_NO_ASYNC(void, InterpreterRuntime::monitorenter(JavaThread* thread, BasicObjectLock* elem))

#ifdef ASSERT

  thread->last_frame().interpreter_frame_verify_monitor(elem);

#endif

  if (PrintBiasedLockingStatistics) {

    Atomic::inc(BiasedLocking::slow_path_entry_count_addr());

  }

  Handle h_obj(thread, elem->obj());

  assert(Universe::heap()->is_in_reserved_or_null(h_obj()),

         "must be NULL or an object");

  if (UseBiasedLocking) {

    // Retry fast entry if bias is revoked to avoid unnecessary inflation

    //开启偏向锁之后会走这个逻辑

    ObjectSynchronizer::fast_enter(h_obj, elem->lock(), true, CHECK);

  } else {

    ObjectSynchronizer::slow_enter(h_obj, elem->lock(), CHECK);

  }

  assert(Universe::heap()->is_in_reserved_or_null(elem->obj()),

         "must be NULL or an object");

#ifdef ASSERT

  thread->last_frame().interpreter_frame_verify_monitor(elem);

#endif

IRT_END
```
## 开启偏向锁执行逻辑ObjectSynchronizer::fast_enter
```
void ObjectSynchronizer::fast_enter(Handle obj, BasicLock* lock, bool attempt_rebias, TRAPS) {
 //判断是否开启了偏向锁
 if (UseBiasedLocking) { 
     //如果不处于全局安全点
    if (!SafepointSynchronize::is_at_safepoint()) {
      //通过`revoke_and_rebias`这个函数尝试获取偏向锁
      BiasedLocking::Condition cond = BiasedLocking::revoke_and_rebias(obj, attempt_rebias, THREAD);
      //如果是撤销与重偏向直接返回
      if (cond == BiasedLocking::BIAS_REVOKED_AND_REBIASED) {
        return;
      }
    } else {//如果在安全点，撤销偏向锁
      assert(!attempt_rebias, "can not rebias toward VM thread");
      BiasedLocking::revoke_at_safepoint(obj);
    }
    assert(!obj->mark()->has_bias_pattern(), "biases should be revoked by now");
 }

 slow_enter (obj, lock, THREAD) ;
}

```
fast_enter实现简单流程：
- 再次检查偏向锁是否已开启
- 当处于不安全点时，通过 revoke_and_rebias尝试获取偏向锁，如果成功则直接返回，如果失败则进入轻量级锁获取过程
- revoke_and_rebias这个偏向锁的获取逻辑在 biasedLocking.cpp中
- 如果偏向锁未开启，则进入 slow_enter获取轻量级锁的流程

## 偏向锁获取逻辑
```
BiasedLocking::Condition BiasedLocking::revoke_and_rebias(Handle obj, bool attempt_rebias, TRAPS) {
  assert(!SafepointSynchronize::is_at_safepoint(), "must not be called while at safepoint");
  markOop mark = obj->mark(); //获取锁对象的对象头
  //判断mark是否为可偏向状态，即mark的偏向锁标志位为1，锁标志位为 01，线程id为null
  if (mark->is_biased_anonymously() && !attempt_rebias) {
    //这个分支是进行对象的hashCode计算时会进入，在一个非全局安全点进行偏向锁撤销
    markOop biased_value       = mark;
    //创建一个非偏向的markword
    markOop unbiased_prototype = markOopDesc::prototype()->set_age(mark->age());
    //Atomic:cmpxchg_ptr是CAS操作，通过cas重新设置偏向锁状态
    markOop res_mark = (markOop) Atomic::cmpxchg_ptr(unbiased_prototype, obj->mark_addr(), mark);
    if (res_mark == biased_value) {//如果CAS成功，返回偏向锁撤销状态
      return BIAS_REVOKED;
    }
  } else if (mark->has_bias_pattern()) {//如果锁对象为可偏向状态（biased_lock:1, lock:01，不管线程id是否为空）,尝试重新偏向
    Klass* k = obj->klass(); 
    markOop prototype_header = k->prototype_header();
    //如果已经有线程对锁对象进行了全局锁定，则取消偏向锁操作
    if (!prototype_header->has_bias_pattern()) {
      markOop biased_value       = mark;
      //CAS 更新对象头markword为非偏向锁
      markOop res_mark = (markOop) Atomic::cmpxchg_ptr(prototype_header, obj->mark_addr(), mark);
      assert(!(*(obj->mark_addr()))->has_bias_pattern(), "even if we raced, should still be revoked");
      return BIAS_REVOKED; //返回偏向锁撤销状态
    } else if (prototype_header->bias_epoch() != mark->bias_epoch()) {
      //如果偏向锁过期，则进入当前分支
      if (attempt_rebias) {//如果允许尝试获取偏向锁
        assert(THREAD->is_Java_thread(), "");
        markOop biased_value       = mark;
        markOop rebiased_prototype = markOopDesc::encode((JavaThread*) THREAD, mark->age(), prototype_header->bias_epoch());
        //通过CAS 操作， 将本线程的 ThreadID 、时间错、分代年龄尝试写入对象头中
        markOop res_mark = (markOop) Atomic::cmpxchg_ptr(rebiased_prototype, obj->mark_addr(), mark);
        if (res_mark == biased_value) { //CAS成功，则返回撤销和重新偏向状态
          return BIAS_REVOKED_AND_REBIASED;
        }
      } else {//不尝试获取偏向锁，则取消偏向锁
        //通过CAS操作更新分代年龄
        markOop biased_value       = mark;
        markOop unbiased_prototype = markOopDesc::prototype()->set_age(mark->age());
        markOop res_mark = (markOop) Atomic::cmpxchg_ptr(unbiased_prototype, obj->mark_addr(), mark);
        if (res_mark == biased_value) { //如果CAS操作成功，返回偏向锁撤销状态
          return BIAS_REVOKED;
        }
      }
    }
  }

}
```

## 偏向锁撤销逻辑
```
void BiasedLocking::revoke_at_safepoint(Handle h_obj) {
  assert(SafepointSynchronize::is_at_safepoint(), "must only be called while at safepoint");
  oop obj = h_obj();
  //更新撤销偏向锁计数，并返回偏向锁撤销次数和偏向次数
  HeuristicsResult heuristics = update_heuristics(obj, false);
  if (heuristics == HR_SINGLE_REVOKE) {//可偏向且未达到批量处理的阈值(下面会单独解释)
    revoke_bias(obj, false, false, NULL); //撤销偏向锁
  } else if ((heuristics == HR_BULK_REBIAS) || 
             (heuristics == HR_BULK_REVOKE)) {//如果是多次撤销或者多次偏向
    //批量撤销
    bulk_revoke_or_rebias_at_safepoint(obj, (heuristics == HR_BULK_REBIAS), false, NULL);
  }
  clean_up_cached_monitor_info();
}

```
偏向锁的释放，需要等待全局安全点（在这个时间点上没有正在执行的字节码），首先暂停拥有偏向锁的线程，然后检查持有偏向锁的线程是否还活着，如果线程不处于活动状态，则将对象头设置成无锁状态。如果线程仍然活着，则会升级为轻量级锁，遍历偏向对象的所记录。栈帧中的锁记录和对象头的Mark Word要么重新偏向其他线程，要么恢复到无锁，或者标记对象不适合作为偏向锁。最后唤醒暂停的线程。

## 轻量级锁获取逻辑
```
void ObjectSynchronizer::slow_enter(Handle obj, BasicLock* lock, TRAPS) {
  markOop mark = obj->mark();
  assert(!mark->has_bias_pattern(), "should not see bias pattern here");

  if (mark->is_neutral()) { //如果当前是无锁状态, markword的biase_lock:0，lock:01
    //直接把mark保存到BasicLock对象的_displaced_header字段
    lock->set_displaced_header(mark);
    //通过CAS将mark word更新为指向BasicLock对象的指针，更新成功表示获得了轻量级锁
    if (mark == (markOop) Atomic::cmpxchg_ptr(lock, obj()->mark_addr(), mark)) {
      TEVENT (slow_enter: release stacklock) ;
      return ;
    }
    // Fall through to inflate() ... 
  }
  //如果markword处于加锁状态、且markword中的ptr指针指向当前线程的栈帧，表示为重入操作，不需要争抢锁 
  else if (mark->has_locker() && THREAD->is_lock_owned((address)mark->locker())) {
    assert(lock != mark->locker(), "must not re-lock the same lock");
    assert(lock != (BasicLock*)obj->mark(), "don't relock with same BasicLock");
    lock->set_displaced_header(NULL);
    return;
  }

#if 0
  // The following optimization isn't particularly useful.
  if (mark->has_monitor() && mark->monitor()->is_entered(THREAD)) {
    lock->set_displaced_header (NULL) ;
    return ;
  }
#endif
  //代码执行到这里，说明有多个线程竞争轻量级锁，轻量级锁通过`inflate`进行膨胀升级为重量级锁
  lock->set_displaced_header(markOopDesc::unused_mark());
  ObjectSynchronizer::inflate(THREAD, obj())->enter(THREAD);
}

```

###轻量级锁释放逻辑monitorexit
```
IRT_ENTRY_NO_ASYNC(void, InterpreterRuntime::monitorexit(JavaThread* thread, BasicObjectLock* elem))

#ifdef ASSERT

  thread->last_frame().interpreter_frame_verify_monitor(elem);

#endif

  Handle h_obj(thread, elem->obj());

  assert(Universe::heap()->is_in_reserved_or_null(h_obj()),

         "must be NULL or an object");

  if (elem == NULL || h_obj()->is_unlocked()) {

    THROW(vmSymbols::java_lang_IllegalMonitorStateException());

  }

  ObjectSynchronizer::slow_exit(h_obj(), elem->lock(), thread);

  // Free entry. This must be done here, since a pending exception might be installed on

  // exit. If it is not cleared, the exception handling code will try to unlock the monitor again.

  elem->set_obj(NULL);

#ifdef ASSERT

  thread->last_frame().interpreter_frame_verify_monitor(elem);

#endif

IRT_END
```
这段代码中主要是通过 ObjectSynchronizer::slow_exit来执行，具体实现如下：

```
void ObjectSynchronizer::slow_exit(oop object, BasicLock* lock, TRAPS) {
  fast_exit (object, lock, THREAD) ;
}
```
ObjectSynchronizer::fast_exit的代码如下：
```
void ObjectSynchronizer::fast_exit(oop object, BasicLock* lock, TRAPS) {
  assert(!object->mark()->has_bias_pattern(), "should not see bias pattern here");
  // if displaced header is null, the previous enter is recursive enter, no-op
  markOop dhw = lock->displaced_header(); //获取锁对象中的对象头
  markOop mark ;
  if (dhw == NULL) { 
     // Recursive stack-lock.
     // Diagnostics -- Could be: stack-locked, inflating, inflated.
     mark = object->mark() ;
     assert (!mark->is_neutral(), "invariant") ;
     if (mark->has_locker() && mark != markOopDesc::INFLATING()) {
        assert(THREAD->is_lock_owned((address)mark->locker()), "invariant") ;
     }
     if (mark->has_monitor()) {
        ObjectMonitor * m = mark->monitor() ;
        assert(((oop)(m->object()))->mark() == mark, "invariant") ;
        assert(m->is_entered(THREAD), "invariant") ;
     }
     return ;
  }

  mark = object->mark() ; //获取线程栈帧中锁记录(LockRecord)中的markword

  // If the object is stack-locked by the current thread, try to
  // swing the displaced header from the box back to the mark.
  if (mark == (markOop) lock) {
     assert (dhw->is_neutral(), "invariant") ;
     //通过CAS尝试将Displaced Mark Word替换回对象头，如果成功，表示锁释放成功。
     if ((markOop) Atomic::cmpxchg_ptr (dhw, object->mark_addr(), mark) == mark) {
        TEVENT (fast_exit: release stacklock) ;
        return;
     }
  }
  //锁膨胀，调用重量级锁的释放锁方法
  ObjectSynchronizer::inflate(THREAD, object)->exit (true, THREAD) ;
}

```
轻量级锁的释放也比较简单，就是将当前线程栈帧中锁记录空间中的Mark Word替换到锁对象的对象头中，如果成功表示锁释放成功。否则，锁膨胀成重量级锁，实现重量级锁的释放锁逻辑。
## 锁膨胀过程分析
```
ObjectMonitor * ATTR ObjectSynchronizer::inflate (Thread * Self, oop object) {
  // Inflate mutates the heap ...
  // Relaxing assertion for bug 6320749.
  assert (Universe::verify_in_progress() ||
          !SafepointSynchronize::is_at_safepoint(), "invariant") ;

  for (;;) { //通过无意义的循环实现自旋操作
      const markOop mark = object->mark() ;
      assert (!mark->has_bias_pattern(), "invariant") ;

      if (mark->has_monitor()) {//has_monitor是markOop.hpp中的方法，如果为true表示当前锁已经是重量级锁了
          ObjectMonitor * inf = mark->monitor() ;//获得重量级锁的对象监视器直接返回
          assert (inf->header()->is_neutral(), "invariant");
          assert (inf->object() == object, "invariant") ;
          assert (ObjectSynchronizer::verify_objmon_isinpool(inf), "monitor is invalid");
          return inf ;
      }

      if (mark == markOopDesc::INFLATING()) {//膨胀等待，表示存在线程正在膨胀，通过continue进行下一轮的膨胀
         TEVENT (Inflate: spin while INFLATING) ;
         ReadStableMark(object) ;
         continue ;
      }

      if (mark->has_locker()) {//表示当前锁为轻量级锁，以下是轻量级锁的膨胀逻辑
          ObjectMonitor * m = omAlloc (Self) ;//获取一个可用的ObjectMonitor
          // Optimistically prepare the objectmonitor - anticipate successful CAS
          // We do this before the CAS in order to minimize the length of time
          // in which INFLATING appears in the mark.
          m->Recycle();
          m->_Responsible  = NULL ;
          m->OwnerIsThread = 0 ;
          m->_recursions   = 0 ;
          m->_SpinDuration = ObjectMonitor::Knob_SpinLimit ;   // Consider: maintain by type/class
          /**将object->mark_addr()和mark比较，如果这两个值相等，则将object->mark_addr()
          改成markOopDesc::INFLATING()，相等返回是mark，不相等返回的是object->mark_addr()**/
                     markOop cmp = (markOop) Atomic::cmpxchg_ptr (markOopDesc::INFLATING(), object->mark_addr(), mark) ;
          if (cmp != mark) {//CAS失败
             omRelease (Self, m, true) ;//释放监视器
             continue ;       // 重试
          }

          markOop dmw = mark->displaced_mark_helper() ;
          assert (dmw->is_neutral(), "invariant") ;

          //CAS成功以后，设置ObjectMonitor相关属性
          m->set_header(dmw) ;


          m->set_owner(mark->locker());
          m->set_object(object);
          // TODO-FIXME: assert BasicLock->dhw != 0.


          guarantee (object->mark() == markOopDesc::INFLATING(), "invariant") ;
          object->release_set_mark(markOopDesc::encode(m));


          if (ObjectMonitor::_sync_Inflations != NULL) ObjectMonitor::_sync_Inflations->inc() ;
          TEVENT(Inflate: overwrite stacklock) ;
          if (TraceMonitorInflation) {
            if (object->is_instance()) {
              ResourceMark rm;
              tty->print_cr("Inflating object " INTPTR_FORMAT " , mark " INTPTR_FORMAT " , type %s",
                (void *) object, (intptr_t) object->mark(),
                object->klass()->external_name());
            }
          }
          return m ; //返回ObjectMonitor
      }
      //如果是无锁状态
      assert (mark->is_neutral(), "invariant");
      ObjectMonitor * m = omAlloc (Self) ; 获取一个可用的ObjectMonitor
      //设置ObjectMonitor相关属性
      m->Recycle();
      m->set_header(mark);
      m->set_owner(NULL);
      m->set_object(object);
      m->OwnerIsThread = 1 ;
      m->_recursions   = 0 ;
      m->_Responsible  = NULL ;
      m->_SpinDuration = ObjectMonitor::Knob_SpinLimit ;       // consider: keep metastats by type/class
      /**将object->mark_addr()和mark比较，如果这两个值相等，则将object->mark_addr()
          改成markOopDesc::encode(m)，相等返回是mark，不相等返回的是object->mark_addr()**/
      if (Atomic::cmpxchg_ptr (markOopDesc::encode(m), object->mark_addr(), mark) != mark) {
          //CAS失败，说明出现了锁竞争，则释放监视器重行竞争锁
          m->set_object (NULL) ;
          m->set_owner  (NULL) ;
          m->OwnerIsThread = 0 ;
          m->Recycle() ;
          omRelease (Self, m, true) ;
          m = NULL ;
          continue ;
          // interference - the markword changed - just retry.
          // The state-transitions are one-way, so there's no chance of
          // live-lock -- "Inflated" is an absorbing state.
      }

      if (ObjectMonitor::_sync_Inflations != NULL) ObjectMonitor::_sync_Inflations->inc() ;
      TEVENT(Inflate: overwrite neutral) ;
      if (TraceMonitorInflation) {
        if (object->is_instance()) {
          ResourceMark rm;
          tty->print_cr("Inflating object " INTPTR_FORMAT " , mark " INTPTR_FORMAT " , type %s",
            (void *) object, (intptr_t) object->mark(),
            object->klass()->external_name());
        }
      }
      return m ; //返回ObjectMonitor对象
  }
}

```
## 重量级锁的竞争逻辑
```
void ATTR ObjectMonitor::enter(TRAPS) {
  // The following code is ordered to check the most common cases first
  // and to reduce RTS->RTO cache line upgrades on SPARC and IA32 processors.
  Thread * const Self = THREAD ;
  void * cur ;

  cur = Atomic::cmpxchg_ptr (Self, &_owner, NULL) ;
  if (cur == NULL) {//CAS成功
     // Either ASSERT _recursions == 0 or explicitly set _recursions = 0.
     assert (_recursions == 0   , "invariant") ;
     assert (_owner      == Self, "invariant") ;
     // CONSIDER: set or assert OwnerIsThread == 1
     return ;
  }

  if (cur == Self) {
     // TODO-FIXME: check for integer overflow!  BUGID 6557169.
     _recursions ++ ;
     return ;
  }

  if (Self->is_lock_owned ((address)cur)) {
    assert (_recursions == 0, "internal state error");
    _recursions = 1 ;
    // Commute owner from a thread-specific on-stack BasicLockObject address to
    // a full-fledged "Thread *".
    _owner = Self ;
    OwnerIsThread = 1 ;
    return ;
  }

  // We've encountered genuine contention.
  assert (Self->_Stalled == 0, "invariant") ;
  Self->_Stalled = intptr_t(this) ;

  // Try one round of spinning *before* enqueueing Self
  // and before going through the awkward and expensive state
  // transitions.  The following spin is strictly optional ...
  // Note that if we acquire the monitor from an initial spin
  // we forgo posting JVMTI events and firing DTRACE probes.
  if (Knob_SpinEarly && TrySpin (Self) > 0) {
     assert (_owner == Self      , "invariant") ;
     assert (_recursions == 0    , "invariant") ;
     assert (((oop)(object()))->mark() == markOopDesc::encode(this), "invariant") ;
     Self->_Stalled = 0 ;
     return ;
  }

  assert (_owner != Self          , "invariant") ;
  assert (_succ  != Self          , "invariant") ;
  assert (Self->is_Java_thread()  , "invariant") ;
  JavaThread * jt = (JavaThread *) Self ;
  assert (!SafepointSynchronize::is_at_safepoint(), "invariant") ;
  assert (jt->thread_state() != _thread_blocked   , "invariant") ;
  assert (this->object() != NULL  , "invariant") ;
  assert (_count >= 0, "invariant") ;

  // Prevent deflation at STW-time.  See deflate_idle_monitors() and is_busy().
  // Ensure the object-monitor relationship remains stable while there's contention.
  Atomic::inc_ptr(&_count);

  EventJavaMonitorEnter event;

  { // Change java thread status to indicate blocked on monitor enter.
    JavaThreadBlockedOnMonitorEnterState jtbmes(jt, this);

    DTRACE_MONITOR_PROBE(contended__enter, this, object(), jt);
    if (JvmtiExport::should_post_monitor_contended_enter()) {
      JvmtiExport::post_monitor_contended_enter(jt, this);
    }

    OSThreadContendState osts(Self->osthread());
    ThreadBlockInVM tbivm(jt);

    Self->set_current_pending_monitor(this);

    // TODO-FIXME: change the following for(;;) loop to straight-line code.
    for (;;) {
      jt->set_suspend_equivalent();
      // cleared by handle_special_suspend_equivalent_condition()
      // or java_suspend_self()

      EnterI (THREAD) ;

      if (!ExitSuspendEquivalent(jt)) break ;

      //
      // We have acquired the contended monitor, but while we were
      // waiting another thread suspended us. We don't want to enter
      // the monitor while suspended because that would surprise the
      // thread that suspended us.
      //
          _recursions = 0 ;
      _succ = NULL ;
      exit (false, Self) ;

      jt->java_suspend_self();
    }
    Self->set_current_pending_monitor(NULL);
  }

```

如果获取锁失败，则需要通过自旋的方式等待锁释放，自旋执行的方法是 ObjectMonitor::EnterI，部分原理以及代码如下：
- 将当前线程封装成ObjectWaiter对象node，状态设置成TS_CXQ。
- 通过自旋操作将node节点push到_cxq队列。
- node节点添加到_cxq队列之后，继续通过自旋尝试获取锁，如果在指定的阈值范围内没有获得锁，则通过park将当前线程挂起，等待被唤醒。

```
void ATTR ObjectMonitor::EnterI (TRAPS) {
    Thread * Self = THREAD ;
    ...//省略很多代码
    ObjectWaiter node(Self) ;
    Self->_ParkEvent->reset() ;
    node._prev   = (ObjectWaiter *) 0xBAD ;
    node.TState  = ObjectWaiter::TS_CXQ ;

    // Push "Self" onto the front of the _cxq.
    // Once on cxq/EntryList, Self stays on-queue until it acquires the lock.
    // Note that spinning tends to reduce the rate at which threads
    // enqueue and dequeue on EntryList|cxq.
    ObjectWaiter * nxt ;
    for (;;) { //自旋，讲node添加到_cxq队列
        node._next = nxt = _cxq ;
        if (Atomic::cmpxchg_ptr (&node, &_cxq, nxt) == nxt) break ;

        // Interference - the CAS failed because _cxq changed.  Just retry.
        // As an optional optimization we retry the lock.
        if (TryLock (Self) > 0) {
            assert (_succ != Self         , "invariant") ;
            assert (_owner == Self        , "invariant") ;
            assert (_Responsible != Self  , "invariant") ;
            return ;
        }
    }
    ...//省略很多代码
    //node节点添加到_cxq队列之后，继续通过自旋尝试获取锁，如果在指定的阈值范围内没有获得锁，则通过park将当前线程挂起，等待被唤醒
    for (;;) {
        if (TryLock (Self) > 0) break ;
        assert (_owner != Self, "invariant") ;

        if ((SyncFlags & 2) && _Responsible == NULL) {
           Atomic::cmpxchg_ptr (Self, &_Responsible, NULL) ;
        }

        // park self //通过park挂起当前线程
        if (_Responsible == Self || (SyncFlags & 1)) {
            TEVENT (Inflated enter - park TIMED) ;
            Self->_ParkEvent->park ((jlong) RecheckInterval) ;
            // Increase the RecheckInterval, but clamp the value.
            RecheckInterval *= 8 ;
            if (RecheckInterval > 1000) RecheckInterval = 1000 ;
        } else {
            TEVENT (Inflated enter - park UNTIMED) ;
            Self->_ParkEvent->park() ;//当前线程挂起
        }

        if (TryLock(Self) > 0) break ; //当线程被唤醒时，会从这里继续执行


        TEVENT (Inflated enter - Futile wakeup) ;
        if (ObjectMonitor::_sync_FutileWakeups != NULL) {
           ObjectMonitor::_sync_FutileWakeups->inc() ;
        }
        ++ nWakeups ;

        if ((Knob_SpinAfterFutile & 1) && TrySpin (Self) > 0) break ;

        if ((Knob_ResetEvent & 1) && Self->_ParkEvent->fired()) {
           Self->_ParkEvent->reset() ;
           OrderAccess::fence() ;
        }
        if (_succ == Self) _succ = NULL ;

        // Invariant: after clearing _succ a thread *must* retry _owner before parking.
        OrderAccess::fence() ;
    }
}

```