/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package ols;

/**
 *
 * @author mac
 */
public class OLSConfig {
    
    private OLSConfig() {
    }
    
    public static OLSConfig getInstance() {
        return OLSConfigHolder.INSTANCE;
    }
    
    private static class OLSConfigHolder {

        private static final OLSConfig INSTANCE = new OLSConfig();
    }
}
