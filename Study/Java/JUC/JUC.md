# LockSupport
## 是什么
用来创建锁和其他同步类的基本线程阻塞原语
## 能干嘛
park() 和 unpark() 的作用分别是==阻塞线程==和==解除阻塞线程==
## 原理是啥
调用LockSupport.park()时

![[Pasted image 20230518113328.png]]
 
permit默认是零，所以一开始调用park()方法，当前线程就会阻塞，直到别的线程将当前线程的permit设置为1时，park方法会被唤醒，然后会将permit再次设置为零并返回。

LockSupport.unpark(thread);
![[Pasted image 20230518113417.png]]
调用unpark(thread)方法后，就会将thread线程的许可permit设置成1(注意多次调用unpark方法，不会累加，permit值还是1)会自动唤醒thread线程，即之前阻塞中的LockSupport.park()方法会立即返回。

先LockSupport.unpark(thread);在park
unpark将permit设置为1，然后在park就会失效

# JMM
## 是什么
因为有这么多级的缓存(cpu和物理主内存的速度不一致的)，
CPU的运行并不是直接操作内存而是先把内存里边的数据读到缓存，而内存的读和写操作的时候就会造成不一致的问题
 ![[Pasted image 20230518114030.png]]
Java虚拟机规范中试图定义一种Java内存模型（java Memory Model，简称JMM) 来屏==**蔽掉各种硬件和操作系统的内存访问差异**==，以实现让Java程序在各种平台下都能达到一致的内存访问效果。推导出我们需要知道JMM
JMM(Java内存模型Java Memory Model，简称JMM)本身是一种抽象的概念并不真实存在它仅仅描述的是一组约定或规范，通过这组规范定义了程序中(尤其是多线程)各个变量的读写访问方式并决定一个线程对共享变量的写入何时以及如何变成对另一个线程可见，关键技术点都是围绕多线程的原子性、可见性和有序性展开的。
## 能干嘛
==通过JMM来实现线程和主内存之间的抽象关系。
屏蔽各个硬件平台和操作系统的内存访问差异以实现让Java程序在各种平台下都能达到一致的内存访问效果。==
## 原理是啥
### 可见性
是指当一个线程修改了某一个共享变量的值，其他线程是否能够立即知道该变更 ，JMM规定了所有的变量都存储在主内存中。
![[Pasted image 20230518114633.png]]

### 原子性
指一个操作是不可中断的，即多线程环境下，操作不能被其他线程干扰
 
 ### 有序性
 对于一个线程的执行代码而言，我们总是习惯性认为代码的执行总是从上到下，有序执行。
但为了提供性能，编译器和处理器通常会对指令序列进行重新排序。
指令重排可以保证串行语义一致，但没有义务保证多线程间的语义也一致，即可能产生"脏读"，简单说，
两行以上不相干的代码在执行的时候有可能先执行的不是第一条，不见得是从上到下顺序执行，执行顺序会被优化。
[Open file:](Study/Java/JUC/assets/6eac2f03a9c25393148bd7931a7120f5_MD5.png)
![](Study/Java/JUC/assets/6eac2f03a9c25393148bd7931a7120f5_MD5.png)
单线程环境里面确保程序最终执行结果和代码顺序执行的结果一致。
处理器在进行重排序时必须要考虑指令之间的数据依赖性
多线程环境中线程交替执行,由于编译器优化重排的存在，两个线程中使用的变量能否保证一致性是无法确定的,结果无法预测
 
### JMM规范下，多线程对变量的读写过程
读取过程：
由于JVM运行程序的实体是线程，而每个线程创建时JVM都会为其创建一个工作内存(有些地方称为栈空间)，工作内存是每个线程的私有数据区域，而Java内存模型中规定所有变量都存储在主内存，主内存是共享内存区域，所有线程都可以访问，但线程对变量的操作(读取赋值等)必须在工作内存中进行，首先要将变量从主内存拷贝到的线程自己的工作内存空间，然后对变量进行操作，操作完成后再将变量写回主内存，不能直接操作主内存中的变量，各个线程中的工作内存中存储着主内存中的变量副本拷贝，因此不同的线程间无法访问对方的工作内存，线程间的通信(传值)必须通过主内存来完成，其简要访问过程如下图:
[Open file:](Study/Java/JUC/assets/2e6f8a8b8e06ad7cda1915752bb8207a_MD5.png)
![](Study/Java/JUC/assets/2e6f8a8b8e06ad7cda1915752bb8207a_MD5.png)
JMM定义了线程和主内存之间的抽象关系
1 线程之间的共享变量存储在主内存中(从硬件角度来说就是内存条)
2 每个线程都有一个私有的本地工作内存，本地工作内存中存储了该线程用来读/写共享变量的副本(从硬件角度来说就是CPU的缓存，比如寄存器、L1、L2、L3缓存等)
 
### JMM规范下，多线程先行发生原则之happens-before
次序规则：
一个线程内，按照代码顺序，写在前面的操作先行发生于写在后面的操作

锁定规则：
一个unLock操作先行发生于后面((这里的“后面”是指时间上的先后))对同一个锁的lock操作；

传递规则：
如果操作A先行发生于操作B，而操作B又先行发生于操作C，则可以得出操作A先行发生于操作C；

线程启动规则(Thread Start Rule)：
Thread对象的start()方法先行发生于此线程的每一个动作

线程中断规则(Thread Interruption Rule)：
对线程interrupt()方法的调用先行发生于被中断线程的代码检测到中断事件的发生；

线程终止规则(Thread Termination Rule)：
线程中的所有操作都先行发生于对此线程的终止检测，我们可以通过Thread::join()方法是否结束、Thread::isAlive()的返回值等手段检测线程是否已经终止执行。

对象终结规则(Finalizer Rule)：
一个对象的初始化完成（构造函数执行结束）先行发生于它的finalize()方法的开始

# volatile与Java内存模型
## 是什么
`volatile`是一个关键字，用于修饰变量
## 能干嘛
可以修饰变量
保证可见性，禁止指令重排序
## 原理
### 可见性
保证不同线程对这个变量进行操作时的可见性，即变量一旦改变所有线程立即可见

Java内存模型中定义的8种工作内存与主内存之间的原子操作
read(读取)→load(加载)→use(使用)→assign(赋值)→store(存储)→write(写入)→lock(锁定)→unlock(解锁)
 ![[Pasted image 20230518153704.png]]
read: 作用于主内存，将变量的值从主内存传输到工作内存，主内存到工作内存
load: 作用于工作内存，将read从主内存传输的变量值放入工作内存变量副本中，即数据加载
use: 作用于工作内存，将工作内存变量副本的值传递给执行引擎，每当JVM遇到需要该变量的字节码指令时会执行该操作
assign: 作用于工作内存，将从执行引擎接收到的值赋值给工作内存变量，每当JVM遇到一个给变量赋值字节码指令时会执行该操作
store: 作用于工作内存，将赋值完毕的工作变量的值写回给主内存
write: 作用于主内存，将store传输过来的变量值赋值给主内存中的变量
由于上述只能保证单条指令的原子性，针对多条指令的组合性原子保证，没有大面积加锁，所以，JVM提供了另外两个原子指令：
lock: 作用于主内存，将一个变量标记为一个线程独占的状态，只是写时候加锁，就只是锁了写变量的过程。
unlock: 作用于主内存，把一个处于锁定状态的变量释放，然后才能被其他线程占用

### 不保证原子性
![[Pasted image 20230518153901.png]]
多线程环境下，"数据计算"和"数据赋值"操作可能多次出现，即操作非原子。若数据在加载之后，若主内存count变量发生修改之后，由于线程工作内存中的值在此前已经加载，从而不会对变更操作做出相应变化，即私有内存和公共内存中变量不同步，进而导致数据不一致
对于volatile变量，JVM只是保证从主内存加载到线程工作内存的值是最新的，也就是数据加载时是最新的。
由此可见volatile解决的是变量读时的可见性问题，但无法保证原子性，对于多线程修改共享变量的场景必须使用加锁同步


[Open file:](Study/Java/JUC/assets/9bf24def1b5f296ddd88c5b71aa2cf73_MD5.png)
![](Study/Java/JUC/assets/9bf24def1b5f296ddd88c5b71aa2cf73_MD5.png)

 read-load-use 和 assign-store-write 成为了两个不可分割的原子操作，但是在use和assign之间依然有极小的一段真空期，有可能变量会被其他线程读取，导致写丢失一次...o(╥﹏╥)o
但是无论在哪一个时间点主内存的变量和任一工作内存的变量的值都是相等的。这个特性就导致了volatile变量不适合参与到依赖当前值的运算，如i = i + 1; i++;之类的那么依靠可见性的特点volatile可以用在哪些地方呢？ 通常volatile用做保存某个状态的boolean值or int值。
《深入理解Java虚拟机》提到：
 ![[Pasted image 20230518154350.png]]
### 指令禁重排
![[Pasted image 20230518154813.png]]
![[Pasted image 20230518154819.png]]

[Open file:](Study/Java/JUC/assets/7be516bcf6c2f555bc47dac60002cb58_MD5.png)
![](Study/Java/JUC/assets/7be516bcf6c2f555bc47dac60002cb58_MD5.png)

![[Pasted image 20230518154901.png]]
![[Pasted image 20230518154911.png]]

在每一个volatile写操作前面插入一个StoreStore屏障
StoreStore屏障可以保证在volatile写之前，其前面的所有普通写操作都已经刷新到主内存中。
在每一个volatile写操作后面插入一个StoreLoad屏障
StoreLoad屏障的作用是避免volatile写与后面可能有的volatile读/写操作重排序
在每一个volatile读操作后面插入一个LoadLoad屏障
LoadLoad屏障用来禁止处理器把上面的volatile读与下面的普通读重排序。
在每一个volatile读操作后面插入一个LoadStore屏障
LoadStore屏障用来禁止处理器把上面的volatile读与下面的普通写重排序。

![[Pasted image 20230518155459.png]]

![[Pasted image 20230518155634.png]]

![[Pasted image 20230518155647.png]]


# CAS 
## 是什么
compare and swap的缩写，中文翻译成比较并交换,实现并发算法时常用到的一种技术。它包含三个操作数——内存位置、预期原值及更新值。
执行CAS操作的时候，将内存位置的值与预期原值比较：
如果相匹配，那么处理器会自动将该位置值更新为新值，
如果不匹配，处理器不做任何操作，多个线程同时执行CAS操作只有一个会成功。
## 能干嘛

## 原理是啥
CAS （CompareAndSwap） 
CAS有3个操作数，位置内存值V，旧的预期值A，要修改的更新值B。
当且仅当旧的预期值A和内存值V相同时，将内存值V修改为B，否则什么都不做或重来
[Open file:](Study/Java/JUC/assets/2a2121c5f4f749ebf9ab7fe6b8bc40f5_MD5.png)
![](Study/Java/JUC/assets/2a2121c5f4f749ebf9ab7fe6b8bc40f5_MD5.png)