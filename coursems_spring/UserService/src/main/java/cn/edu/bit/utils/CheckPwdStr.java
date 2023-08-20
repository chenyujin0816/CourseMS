package cn.edu.bit.utils;

public class CheckPwdStr {
    public static int getPwdStrength(String pwd){
        int pwdStr = 0;
        if (pwd.length() < 8 || pwd.length() > 16)
            pwdStr = -999;
        int numCount = 0, letterCount = 0, charCount = 0;
        for (int i = 0; i < pwd.length(); i++) {
            if (String.valueOf(pwd.charAt(i)).matches("[0-9]"))
                numCount++;
            else if (String.valueOf(pwd.charAt(i)).matches("[A-Za-z]"))
                letterCount++;
            else
                charCount++;
        }
        if (numCount > 0 && letterCount > 0 && charCount > 0)
            pwdStr += 2;
        else if ((numCount > 0 && letterCount > 0 && charCount == 0) ||
                (numCount > 0 && charCount > 0 && letterCount == 0) ||
                (numCount == 0 && letterCount > 0 && charCount > 0))
            pwdStr += 1;
        return pwdStr;
    }
}
