/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.ui;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import javax.swing.Icon;

import iqq.comm.Auth;
import iqq.comm.Auth.AuthInfo;
import iqq.comm.CloseTabIcon;
import iqq.model.Group;
import iqq.model.Member;
import iqq.model.Message;
import iqq.service.StackMessageService;

/**
 *
 * @author chenzhihui
 */
public class ChatDialog extends javax.swing.JFrame {

    private HashMap tabMaps = new HashMap();
    private AuthInfo loginAI = Auth.getSingleAccountInfo();

    public HashMap getTabMaps() {
        return tabMaps;
    }

    public void setTabMaps(HashMap tabMaps) {
        this.tabMaps = tabMaps;
    }

    /**
     * Creates new form ChatDialog
     */
    public ChatDialog(java.awt.Frame parent, boolean modal) {
        //super(parent, modal);
        initComponents();
        this.setIconImage(loginAI.getMember().getFace().getImage());
        this.validate();
        this.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabChat = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setBounds(new java.awt.Rectangle(350, 160, 0, 0));

        tabChat.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 583, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(2, 2, 2)
                    .addComponent(tabChat, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
                    .addGap(2, 2, 2)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 464, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(8, 8, 8)
                    .addComponent(tabChat, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                    .addGap(9, 9, 9)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChatDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChatDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChatDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChatDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the dialog
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                ChatDialog dialog = new ChatDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabChat;
    // End of variables declaration//GEN-END:variables

    public void addChat(Message msg) throws Exception {
        Member member = msg.getMember();
        Group group = msg.getGroup();
        
        BaseChatPanel p = null;
        String title = "";
        if(member != null) {
            p =new ChatPanel(member); 
            title = member.getNickname();
        } else if(group != null ) {
           p = new GroupChatPanel(group);
           title = group.getName();
        }else {
            return ;
        }
        
        final Icon icon = new CloseTabIcon(this.getClass().getClassLoader().getResource("iqq/res/images/icon/close.png"));
        
        this.addTab(title, icon, p, msg);

        tabChat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int tabNumber = tabChat.getUI().tabForCoordinate(tabChat, e.getX(), e.getY());
                if (tabNumber < 0) {
                    return;
                }
                //Log.println("tabNumber: " + tabNumber);
                Rectangle rect = ((CloseTabIcon) tabChat.getIconAt(tabNumber)).getBounds();
                if (rect.contains(e.getX(), e.getY())) {
                    //the tab is being closed  
                    removeTabAt(tabNumber);
                    if(tabChat.getTabCount() == 0) {
                        dispose();
                    }
                }
            }
        });
    }

    public void addTab(String title, Icon icon, BaseChatPanel component, Message msg) {
        Member member = null;
        Group group = null;
        long id = -2;
        if(msg.getMember() != null) {
            member = msg.getMember();
            id = member.getUin();
        } else if(msg.getGroup() != null){
            group = msg.getGroup();
            id = group.getId();
        }else{
            return ;
        }
        
        String key = "tab_" + id;
        BaseChatPanel c = (BaseChatPanel) tabMaps.get(key);
        if(c != null) {
            if(tabChat.isAncestorOf(c)) {
                if(msg != null) {
                    c.setMessages(msg);
                }
                tabChat.setSelectedComponent(c);
                this.validate();
                this.setVisible(true);
                return ;
            }
            component = c;
        }
        if(msg != null) {
            component.setMessages(msg);
        }
        tabMaps.put(key, component);
        tabChat.addTab(title, icon, component);
        tabChat.setSelectedComponent(component);
        
        this.validate();
        this.setVisible(true);
    }

    public void removeTabAt(int index) {
        
        BaseChatPanel component = (BaseChatPanel)tabChat.getComponentAt(index);
        String key = "tab_" + component.getId();
        tabMaps.remove(key);
        tabChat.removeTabAt(index);
       // Log.println("TabCount: " + tabChat.getTabCount() + " MapCount:" + tabMaps.size() + " key:" + key);
        
        this.validate();
        this.setVisible(true);
    }
    
    public void dispose() {
        StackMessageService.getIntance().setMessageMaps(null);
        if(tabMaps != null) {
            tabMaps.clear();
        }
        if(tabChat != null) {
            tabChat.removeAll();
        }
        this.setVisible(false);
    }
}
