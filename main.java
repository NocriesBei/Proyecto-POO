package com.penascal.main;

import com.penascal.vista.PantallaPrincipal;
import javax.swing.UIManager;

public class main {
    public static void main(String[] args) {
        // Intenta poner un diseño más moderno por defecto
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("No se pudo aplicar el tema visual.");
        }

        // Iniciar la pantalla principal
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PantallaPrincipal frame = new PantallaPrincipal();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
