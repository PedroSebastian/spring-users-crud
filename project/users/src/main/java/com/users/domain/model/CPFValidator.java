package com.users.domain.model;

public class CPFValidator {

    private CPFValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidCPF(String cpf) {
        if (!cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}")) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11 || hasAllSameDigits(cpf)) {
            return false;
        }

        int[] weightsFirstDigit = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] weightsSecondDigit = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int firstDigit = calculateDigit(cpf.substring(0, 9), weightsFirstDigit);
        int secondDigit = calculateDigit(cpf.substring(0, 9) + firstDigit, weightsSecondDigit);

        return cpf.equals(cpf.substring(0, 9) + firstDigit + secondDigit);
    }

    private static int calculateDigit(String str, int[] weights) {
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            int num = Integer.parseInt(str.substring(i, i + 1));
            sum += num * weights[i];
        }

        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    private static boolean hasAllSameDigits(String cpf) {
        return cpf.chars().allMatch(c -> c == cpf.charAt(0));
    }
}
