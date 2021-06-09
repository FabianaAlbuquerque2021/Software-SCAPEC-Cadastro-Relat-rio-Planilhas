/* Tela para mostrar gráfico em pizza mostrando o índice trimestral.
 */
package br.com.cadastroCohab.telas;

//Bibliotecas importadas.
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import java.sql.*;
import br.com.cadastroCohab.dal.ModuloConexao;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;

/**
 *
 * @author Fabiana de Albuquerque
 */
public class TelaGraficoTrimestral extends javax.swing.JInternalFrame {

   //Conexao BD.
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public TelaGraficoTrimestral() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    private void mostrar(){
        // Botão para gerar gráfico em pizza.
        
        //Chamando dados do BD com o select através da variável sql.
        String sql = "SELECT sum(matriculados), sum(ausentes), sum(presentes), sum(visitantes), sum(biblias), sum(revistas), sum(ofertas), sum(ofertasMissoes) FROM tbexcel WHERE EXTRACT(MONTH FROM data_registro) LIKE ? AND EXTRACT(YEAR FROM data_registro) LIKE ?";
        try {
        pst = conexao.prepareStatement(sql);
        pst.setString(1, campoMes.getText()); //Pega o mês do campo
        pst.setString(2, campoAno.getText()); //Pega o ano do campo
        rs = pst.executeQuery();
        
        //Se tiver dados vai mostrar os matriculados, ausentes, presentes, visitantes, biblias, revistas, ofertas e ofertas de missões no gráfico.
        if(rs.next()){
        DefaultPieDataset dpd = new DefaultPieDataset();
        dpd.setValue("Matriculados", rs.getInt(1));
        dpd.setValue("Ausentes", rs.getInt(2));
        dpd.setValue("Presentes", rs.getInt(3));
        dpd.setValue("Visitantes", rs.getInt(4));
        dpd.setValue("Biblias", rs.getInt(5));
        dpd.setValue("Revistas", rs.getInt(6));
        dpd.setValue("Ofertas", rs.getInt(7));
        dpd.setValue("Ofertas de Missões", rs.getInt(8));
        
        //Cria o gráfico em pizza.
        JFreeChart grafico = ChartFactory.createPieChart("Gráfico de Índice Mensal das Classes", dpd, true, true, false);

        ChartPanel chartPanel = new ChartPanel(grafico);

        Painel2.add(chartPanel);
        Painel2.validate();
        //Senão aparece a mensagem de data não encontrada.
        } else{
            JOptionPane.showMessageDialog(null, "Registro de data mencionada não encontrado!");
            
            campoMes.setText(null);
            campoAno.setText(null);
        }
        //Exibe mensagem de erro.
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Painel1 = new javax.swing.JPanel();
        Painel2 = new javax.swing.JPanel();
        botao = new javax.swing.JButton();
        campoMes = new javax.swing.JTextField();
        txtMes = new javax.swing.JLabel();
        txtAno = new javax.swing.JLabel();
        campoAno = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);

        Painel1.setBackground(new java.awt.Color(51, 51, 51));

        Painel2.setBackground(new java.awt.Color(255, 255, 255));
        Painel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Painel2.setLayout(new java.awt.BorderLayout());

        botao.setBackground(new java.awt.Color(255, 204, 0));
        botao.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        botao.setText("Gerar Gráfico");
        botao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoActionPerformed(evt);
            }
        });
        botao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                botaoKeyPressed(evt);
            }
        });

        campoMes.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoMes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoMesKeyPressed(evt);
            }
        });

        txtMes.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtMes.setForeground(new java.awt.Color(255, 255, 255));
        txtMes.setText("MÊS:");

        txtAno.setBackground(new java.awt.Color(255, 255, 255));
        txtAno.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtAno.setForeground(new java.awt.Color(255, 255, 255));
        txtAno.setText("ANO:");

        campoAno.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoAno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoAnoActionPerformed(evt);
            }
        });
        campoAno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoAnoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout Painel1Layout = new javax.swing.GroupLayout(Painel1);
        Painel1.setLayout(Painel1Layout);
        Painel1Layout.setHorizontalGroup(
            Painel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Painel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Painel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Painel1Layout.createSequentialGroup()
                        .addComponent(Painel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Painel1Layout.createSequentialGroup()
                        .addGap(0, 284, Short.MAX_VALUE)
                        .addComponent(txtMes, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(campoMes, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtAno, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(campoAno, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(243, 243, 243))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Painel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botao, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(322, 322, 322))
        );
        Painel1Layout.setVerticalGroup(
            Painel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Painel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(Painel2, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(Painel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(campoMes)
                    .addComponent(txtAno)
                    .addComponent(campoAno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(botao, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Painel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Painel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName("Gráfico Trimestral");

        setBounds(0, 0, 901, 632);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoActionPerformed
       //Chamando método com gráfico.
       mostrar();
    }//GEN-LAST:event_botaoActionPerformed

    private void campoAnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoAnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoAnoActionPerformed

    private void campoMesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoAno.requestFocus();
        }
    }//GEN-LAST:event_campoMesKeyPressed

    private void campoAnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnoKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            botao.requestFocus();
        }
    }//GEN-LAST:event_campoAnoKeyPressed

    private void botaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_botaoKeyPressed
        // Colocando atalho enter para mostrar gráfico.
        if(evt.getKeyCode() == evt.VK_ENTER){
        //Chamando método com gráfico.
        mostrar();
        }
    }//GEN-LAST:event_botaoKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Painel1;
    private javax.swing.JPanel Painel2;
    private javax.swing.JButton botao;
    private javax.swing.JTextField campoAno;
    private javax.swing.JTextField campoMes;
    private javax.swing.JLabel txtAno;
    private javax.swing.JLabel txtMes;
    // End of variables declaration//GEN-END:variables
}
