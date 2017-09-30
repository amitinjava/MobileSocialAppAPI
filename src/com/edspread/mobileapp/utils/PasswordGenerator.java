package com.edspread.mobileapp.utils;

public class PasswordGenerator {
    private static Blowfish cipher = null;
    
    private static synchronized Blowfish getCipher() {
	        if (cipher != null) {
	            return cipher;
	        }
	        // Get the password key, stored as a database property. Obviously,
	        // protecting your database is critical for making the
	        // encryption fully secure.
	        String keyString = "n4Iuqu5u5TExO1G";
	        try {
	           // keyString = JiveGlobals.getProperty("passwordKey");
	            
	            cipher = new Blowfish(keyString);
	        }
	        catch (Exception e) {
	           e.printStackTrace();
	        }
	        return cipher;
	    }
    
    public static String encryptPassword(String password) {
        if (password == null) {
            return null;
        }
        Blowfish cipher = getCipher();
        if (cipher == null) {
            throw new UnsupportedOperationException();
        }
        return cipher.encryptString(password);
    }
    
   
    /**
     * Returns a decrypted version of the encrypted password. Encryption is performed
     * using the Blowfish algorithm. The encryption key is stored as the Jive property
     * "passwordKey". If the key is not present, it will be automatically generated.
     *
     * @param encryptedPassword the encrypted password.
     * @return the encrypted password.
     * @throws UnsupportedOperationException if encryption/decryption is not possible;
     *      for example, during setup mode.
     */
    public static String decryptPassword(String encryptedPassword) {
        if (encryptedPassword == null) {
            return null;
        }
        Blowfish cipher = getCipher();
        if (cipher == null) {
            throw new UnsupportedOperationException();
        }
        return cipher.decryptString(encryptedPassword);
    }
    /*
    public static void main(String[] args) {
		String pwd = "";
		System.out.println("Pwd::::"+encryptPassword("123456"));
		System.out.println("Pwd::::"+decryptPassword("85f1373a3c5c76662f90a514d805b2ace76808890de09e99"));
	}
*/

}
