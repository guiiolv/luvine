package com.luvine.modules.auth.domain.rules;

import com.luvine.common.domain.BusinessRule;

public class CodeMustMatchRule implements BusinessRule {

    private final CodeHash expectedCode;
    private final CodeHash providedCode;

    public CodeMustMatchRule(CodeHash expectedCode, CodeHash providedCode) {
        this.expectedCode = expectedCode;
        this.providedCode = providedCode;
    }

    @Override
    public boolean isBroken() {
        return !expectedCode.equals(providedCode);
    }

    @Override
    public String message() {
        return "Código de verificação inválido.";
    }
}