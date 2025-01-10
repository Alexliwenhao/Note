/*
package com.checkstyle;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.llvm.LLVM.LLVMContextRef;
import org.bytedeco.llvm.LLVM.LLVMModuleRef;
import org.bytedeco.llvm.LLVM.LLVMValueRef;
import org.bytedeco.llvm.LLVM.LLVMVerifierFailureAction;
import org.bytedeco.llvm.global.LLVM;
import org.bytedeco.llvm.global.LLVMClang;

public class ClangDemo {
    public static void main(String[] args) {
        // 加载Clang库
        LLVM.load();

        // 创建LLVM上下文
        LLVMContextRef context = LLVM.LLVMContextCreate();

        // 创建LLVM模块
        LLVMModuleRef module = LLVM.LLVMModuleCreateWithNameInContext("mymodule", context);

        // 读取C代码
        String sourceCode = new String(Files.readAllBytes(Paths.get("test.c")));

        // 编译C代码并获取抽象语法树
        List<String> argsList = new ArrayList<String>();
        argsList.add("-fparse-all-comments");
        String[] args = argsList.toArray(new String[argsList.size()]);
        LLVMValueRef ast = LLVMClang.ClangCreateIndex(0, 0);
        LLVMClang.ClangParseTranslationUnit2(ast, "test.c", args, args.length, null, 0,
                0x0F | 0x10 | 0x20 | 0x40 | 0x80 | 0x100 | 0x400 | 0x800 | 0x1000, module, context);

        // 验证LLVM模块
        String errorMessage = new String();
        int result = LLVM.LLVMVerifyModule(module, LLVMVerifierFailureAction.LLVMPrintMessageAction, errorMessage);
        if (result != 0) {
            System.out.println("Verification failed: " + errorMessage);
            return;
        }

        // 输出LLVM模块中的所有函数
        for (LLVMValueRef function = LLVM.LLVMGetFirstFunction(module); function != null; function = LLVM.LLVMGetNextFunction(function)) {
            System.out.println(LLVM.LLVMGetValueName(function));
        }

        // 释放资源
        LLVM.LLVMDisposeModule(module);
        LLVM.LLVMContextDispose(context);
    }
}*/
