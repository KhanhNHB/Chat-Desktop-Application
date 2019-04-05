package fu.login;

import fu.client.ClientDAO;
import fu.client.ClientDTO;
import javax.swing.JOptionPane;
import fu.application.ChatApplication;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login extends javax.swing.JFrame {

    public Login() {
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        pnlDemoApp = new javax.swing.JPanel();
        lblImageApp = new javax.swing.JLabel();
        lblInfoApp = new javax.swing.JLabel();
        lblSignUp = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        btnLogin = new javax.swing.JButton();
        btnRegister = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImages(null);
        setMaximumSize(new java.awt.Dimension(700, 520));
        setMinimumSize(new java.awt.Dimension(700, 520));
        setPreferredSize(new java.awt.Dimension(700, 520));

        pnlMain.setBackground(new java.awt.Color(255, 255, 255));
        pnlMain.setLayout(null);

        pnlDemoApp.setBackground(new java.awt.Color(0, 102, 204));

        lblImageApp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fu/login/greeting-card.png"))); // NOI18N
        lblImageApp.setText("lbl");

        lblInfoApp.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        lblInfoApp.setForeground(new java.awt.Color(255, 255, 255));
        lblInfoApp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblInfoApp.setText("Chat Application Login");

        javax.swing.GroupLayout pnlDemoAppLayout = new javax.swing.GroupLayout(pnlDemoApp);
        pnlDemoApp.setLayout(pnlDemoAppLayout);
        pnlDemoAppLayout.setHorizontalGroup(
            pnlDemoAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDemoAppLayout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addGroup(pnlDemoAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblInfoApp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblImageApp, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44))
        );
        pnlDemoAppLayout.setVerticalGroup(
            pnlDemoAppLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDemoAppLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(lblImageApp, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(lblInfoApp, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(108, Short.MAX_VALUE))
        );

        pnlMain.add(pnlDemoApp);
        pnlDemoApp.setBounds(0, 0, 353, 488);

        lblSignUp.setFont(new java.awt.Font("Calibri", 0, 36)); // NOI18N
        lblSignUp.setForeground(new java.awt.Color(102, 102, 102));
        lblSignUp.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblSignUp.setText("Login");
        pnlMain.add(lblSignUp);
        lblSignUp.setBounds(400, 10, 240, 44);

        lblPassword.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(51, 51, 51));
        lblPassword.setText("Password");
        pnlMain.add(lblPassword);
        lblPassword.setBounds(400, 190, 130, 14);

        txtPassword.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtPassword.setForeground(new java.awt.Color(51, 51, 51));
        txtPassword.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        pnlMain.add(txtPassword);
        txtPassword.setBounds(400, 210, 230, 40);

        lblUsername.setFont(new java.awt.Font("Calibri", 0, 12)); // NOI18N
        lblUsername.setForeground(new java.awt.Color(51, 51, 51));
        lblUsername.setText("Username");
        pnlMain.add(lblUsername);
        lblUsername.setBounds(400, 90, 130, 16);

        txtUsername.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        txtUsername.setForeground(new java.awt.Color(51, 51, 51));
        txtUsername.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 51, 51)));
        pnlMain.add(txtUsername);
        txtUsername.setBounds(400, 110, 230, 40);

        btnLogin.setBackground(new java.awt.Color(0, 102, 204));
        btnLogin.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Login");
        btnLogin.setBorder(null);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });
        pnlMain.add(btnLogin);
        btnLogin.setBounds(400, 290, 230, 50);

        btnRegister.setBackground(new java.awt.Color(0, 102, 204));
        btnRegister.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        btnRegister.setForeground(new java.awt.Color(255, 255, 255));
        btnRegister.setText("Register");
        btnRegister.setBorder(null);
        btnRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRegister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegisterActionPerformed(evt);
            }
        });
        pnlMain.add(btnRegister);
        btnRegister.setBounds(400, 380, 230, 50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 686, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMain, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        try {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();

            ClientDAO dao = new ClientDAO();

            ClientDTO dto = dao.checkLogin(username, password);

            if (dto != null) {
                this.dispose();
                new ChatApplication(dto);
            } else {
                txtUsername.setText("");
                txtPassword.setText("");
                JOptionPane.showMessageDialog(this, "Incorrect username or password.");
            }
        } catch (SQLException ex) {
            if (ex.getMessage().contains("duplicate key")) {
                JOptionPane.showMessageDialog(this, "Username is exist");
            }
        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnLoginActionPerformed

    private void btnRegisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegisterActionPerformed
        this.dispose();
        new Register();
    }//GEN-LAST:event_btnRegisterActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JButton btnRegister;
    private javax.swing.JLabel lblImageApp;
    private javax.swing.JLabel lblInfoApp;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSignUp;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlDemoApp;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
