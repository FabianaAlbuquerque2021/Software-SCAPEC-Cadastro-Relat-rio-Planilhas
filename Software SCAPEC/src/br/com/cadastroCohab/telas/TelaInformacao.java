/*
Tela para mostrar informações do banco e calcular total.
 */
package br.com.cadastroCohab.telas;

//Bibliotecas importadas.
import java.sql.*;
import br.com.cadastroCohab.dal.ModuloConexao;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Fabiana de Albuquerque
 */
public class TelaInformacao extends javax.swing.JInternalFrame {

    /**
     * Creates new form TelaArmazenamento
     */
    //Conexao BD.
    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    public TelaInformacao() {
        initComponents();
        conexao = ModuloConexao.conector();
    }
    
    //metodo para consultar informações no BD.
    private void consultar() {
        //Código SQL usando o SUM para somar os valores em cada uma das colunas, e usando o EXTRACT para tirar o dia, o mês e o ano que o usuário quer pesquisar o total das informações.
        String sql = "SELECT sum(matriculados), sum(ausentes), sum(presentes), sum(visitantes), sum(totalPresentesVisitantes), sum(biblias), sum(revistas), concat('R$ ', sum(ofertas)), concat('R$ ', sum(ofertasMissoes)) FROM tbexcel WHERE EXTRACT(DAY FROM data_registro) LIKE ? AND EXTRACT(MONTH FROM data_registro) LIKE ? AND EXTRACT(YEAR FROM data_registro) LIKE ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, campoDia.getText());
            pst.setString(2, campoMes.getText());
            pst.setString(3, campoAno.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                campoTotalMatriculados.setText(rs.getString(1));
                campoTotalAusentes.setText(rs.getString(2));
                campoTotalPresentes.setText(rs.getString(3));
                campoTotalVisitantes.setText(rs.getString(4));
                campoTotalPresVis.setText(rs.getString(5));
                campoTotalBiblias.setText(rs.getString(6));
                campoTotalRevistas.setText(rs.getString(7));
                campoTotalOfertas.setText(rs.getString(8));
                campoTotalOfertMis.setText(rs.getString(9));
            } else {
                JOptionPane.showMessageDialog(null, "Registro de data mencionada não encontrado!");
                // as linhas abaixo "limpam" os campos.
                campoTotalMatriculados.setText(null);
                campoTotalAusentes.setText(null);
                campoTotalPresentes.setText(null);
                campoTotalVisitantes.setText(null);
                campoTotalPresVis.setText(null);
                campoTotalBiblias.setText(null);
                campoTotalRevistas.setText(null);
                campoTotalOfertas.setText(null);
                campoTotalOfertMis.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    
    //Segundo método para consultar informações no BD.
    private void subTotalConsulta() {
        //Código SQL usando o SUM para somar os valores em cada uma das colunas, e usando o EXTRACT para tirar o dia, o mês e o ano que o usuário quer pesquisar o total das informações.
        String sql = "SELECT sum(matriculados), sum(ausentes), sum(presentes), sum(visitantes), sum(totalPresentesVisitantes), sum(biblias), sum(revistas), concat('R$ ', sum(ofertas)), concat('R$ ', sum(ofertasMissoes)) FROM tbexcel WHERE EXTRACT(DAY FROM data_registro) LIKE ? AND EXTRACT(MONTH FROM data_registro) LIKE ? AND EXTRACT(YEAR FROM data_registro) LIKE ? AND departamento LIKE ?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, campoDia.getText());
            pst.setString(2, campoMes.getText());
            pst.setString(3, campoAno.getText());
            pst.setString(4, campoDept.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                campoTotalMatriculados.setText(rs.getString(1));
                campoTotalAusentes.setText(rs.getString(2));
                campoTotalPresentes.setText(rs.getString(3));
                campoTotalVisitantes.setText(rs.getString(4));
                campoTotalPresVis.setText(rs.getString(5));
                campoTotalBiblias.setText(rs.getString(6));
                campoTotalRevistas.setText(rs.getString(7));
                campoTotalOfertas.setText(rs.getString(8));
                campoTotalOfertMis.setText(rs.getString(9));
            } else {
                JOptionPane.showMessageDialog(null, "Registro de data mencionada não encontrado!");
                // as linhas abaixo "limpam" os campos.
                campoTotalMatriculados.setText(null);
                campoTotalAusentes.setText(null);
                campoTotalPresentes.setText(null);
                campoTotalVisitantes.setText(null);
                campoTotalPresVis.setText(null);
                campoTotalBiblias.setText(null);
                campoTotalRevistas.setText(null);
                campoTotalOfertas.setText(null);
                campoTotalOfertMis.setText(null);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }
    
    //Método para pegar dados dos campos e enviar para o BD.
    private void enviarDados(){
         String sql = "insert into tbtotais(totalMat,totalAus,totalPres,totalVis,totalPresVis,totalBib,totalOf,totalRev,totalOfMiss,dept,data_dia,data_mes,data_ano) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, campoTotalMatriculados.getText());
            pst.setString(2, campoTotalAusentes.getText());
            pst.setString(3, campoTotalPresentes.getText());
            pst.setString(4, campoTotalVisitantes.getText());
            pst.setString(5, campoTotalPresVis.getText());
            pst.setString(6, campoTotalBiblias.getText());
            pst.setString(7, campoTotalOfertas.getText());
            pst.setString(8, campoTotalRevistas.getText());
            pst.setString(9, campoTotalOfertMis.getText());
            pst.setString(10, campoDept.getText());
            pst.setString(11, campoDia.getText());
            pst.setString(12, campoMes.getText());
            pst.setString(13, campoAno.getText());
            //Validação dos campos obrigatórios.
            if (((((campoTotalMatriculados.getText().isEmpty()) || (campoTotalAusentes.getText().isEmpty()) || (campoTotalPresentes.getText().isEmpty()) || (campoTotalVisitantes.getText().isEmpty()))))) {
                JOptionPane.showMessageDialog(null, "Os campos tem que estar preenchidos! Aperte o botão para pesquisar pela data!");

            } else {

                //A linha abaixo atualiza a tabela usuario com os dados do formulário.
                // A estrutura abaixo é usada para confirmar a inserção dos dados na tabela.
                int adicionado = pst.executeUpdate();
                //A linha abaixo serve de apoio.
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Informações adicionadas com sucesso!");
                    // as linhas abaixo "limpam" os campos.
                    campoTotalMatriculados.setText(null);
                    campoTotalAusentes.setText(null);
                    campoTotalPresentes.setText(null);
                    campoTotalVisitantes.setText(null);
                    campoTotalPresVis.setText(null);
                    campoTotalBiblias.setText(null);
                    campoTotalOfertas.setText(null);
                    campoTotalRevistas.setText(null);
                    campoTotalOfertMis.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Método para impressão de relatório de totais de acordo com a data de hoje.
    private void imprimir_data() {
                // Gerando um relatório de totais comparando com a data atual.
                int confirma = JOptionPane.showConfirmDialog(null, "Confirma a impressão deste relatório sobre os totais das classes enviados na data de hoje?", "Atenção", JOptionPane.YES_NO_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {
                    //Imprimindo relatório com o framework JasperReports.
                    try {
                        //Usando a classe JasperPrint para preparar a impressão de um relatório.
                        JasperPrint print = JasperFillManager.fillReport("C:/reports/informacoesTotal.jasper", null, conexao);
                        //A linha abaixo exibe o relatório de classes através da classe JasperViewer.
                        JasperViewer.viewReport(print, false);
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, e);
                    }
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

        PainelAmarelo = new javax.swing.JPanel();
        txtTitulo = new javax.swing.JLabel();
        PainelCinza = new javax.swing.JPanel();
        txtMatriculados = new javax.swing.JLabel();
        txtAusentes = new javax.swing.JLabel();
        txtPresentes = new javax.swing.JLabel();
        txtVisitantes = new javax.swing.JLabel();
        txtPresentVisitante = new javax.swing.JLabel();
        txtBiblias = new javax.swing.JLabel();
        txtRevistas = new javax.swing.JLabel();
        txtOfertas = new javax.swing.JLabel();
        txtOfertMissoes = new javax.swing.JLabel();
        campoTotalMatriculados = new javax.swing.JTextField();
        campoTotalAusentes = new javax.swing.JTextField();
        campoTotalPresentes = new javax.swing.JTextField();
        campoTotalVisitantes = new javax.swing.JTextField();
        campoTotalPresVis = new javax.swing.JTextField();
        campoTotalBiblias = new javax.swing.JTextField();
        campoTotalRevistas = new javax.swing.JTextField();
        campoTotalOfertas = new javax.swing.JTextField();
        campoTotalOfertMis = new javax.swing.JTextField();
        txtPergunta = new javax.swing.JLabel();
        campoDia = new javax.swing.JTextField();
        botaoPesquisar = new javax.swing.JButton();
        campoMes = new javax.swing.JTextField();
        campoAno = new javax.swing.JTextField();
        txtPerguntaDept = new javax.swing.JLabel();
        campoDept = new javax.swing.JTextField();
        botaoImprimir = new javax.swing.JButton();
        botaoEnviar = new javax.swing.JButton();
        textObs = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Informações de Total");
        setToolTipText("");
        setPreferredSize(new java.awt.Dimension(889, 666));

        PainelAmarelo.setBackground(new java.awt.Color(92, 92, 172));

        txtTitulo.setFont(new java.awt.Font("Centaur", 1, 36)); // NOI18N
        txtTitulo.setForeground(new java.awt.Color(255, 255, 255));
        txtTitulo.setText("Informações do Total das Classes");

        javax.swing.GroupLayout PainelAmareloLayout = new javax.swing.GroupLayout(PainelAmarelo);
        PainelAmarelo.setLayout(PainelAmareloLayout);
        PainelAmareloLayout.setHorizontalGroup(
            PainelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelAmareloLayout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(txtTitulo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PainelAmareloLayout.setVerticalGroup(
            PainelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelAmareloLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(txtTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        PainelCinza.setBackground(new java.awt.Color(204, 204, 204));

        txtMatriculados.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtMatriculados.setForeground(new java.awt.Color(255, 255, 255));
        txtMatriculados.setText("Total de Matriculados:");

        txtAusentes.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtAusentes.setForeground(new java.awt.Color(255, 255, 255));
        txtAusentes.setText("Total de Ausentes:");

        txtPresentes.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPresentes.setForeground(new java.awt.Color(255, 255, 255));
        txtPresentes.setText("Total de Presentes:");

        txtVisitantes.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtVisitantes.setForeground(new java.awt.Color(255, 255, 255));
        txtVisitantes.setText("Total de Visitantes:");

        txtPresentVisitante.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPresentVisitante.setForeground(new java.awt.Color(255, 255, 255));
        txtPresentVisitante.setText("Total de Presentes e Visitantes:");

        txtBiblias.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtBiblias.setForeground(new java.awt.Color(255, 255, 255));
        txtBiblias.setText("Total de Bíblias:");

        txtRevistas.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtRevistas.setForeground(new java.awt.Color(255, 255, 255));
        txtRevistas.setText("Total de Revistas:");

        txtOfertas.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtOfertas.setForeground(new java.awt.Color(255, 255, 255));
        txtOfertas.setText("Total de Ofertas:");

        txtOfertMissoes.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtOfertMissoes.setForeground(new java.awt.Color(255, 255, 255));
        txtOfertMissoes.setText("Total de Ofertas Missões:");

        campoTotalMatriculados.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalMatriculados.setForeground(new java.awt.Color(0, 153, 51));
        campoTotalMatriculados.setEnabled(false);

        campoTotalAusentes.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalAusentes.setForeground(new java.awt.Color(255, 0, 0));
        campoTotalAusentes.setEnabled(false);
        campoTotalAusentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTotalAusentesActionPerformed(evt);
            }
        });

        campoTotalPresentes.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalPresentes.setForeground(new java.awt.Color(0, 204, 204));
        campoTotalPresentes.setEnabled(false);
        campoTotalPresentes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTotalPresentesActionPerformed(evt);
            }
        });

        campoTotalVisitantes.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalVisitantes.setForeground(new java.awt.Color(255, 204, 0));
        campoTotalVisitantes.setEnabled(false);

        campoTotalPresVis.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalPresVis.setForeground(new java.awt.Color(0, 153, 102));
        campoTotalPresVis.setEnabled(false);

        campoTotalBiblias.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalBiblias.setForeground(new java.awt.Color(255, 0, 0));
        campoTotalBiblias.setEnabled(false);

        campoTotalRevistas.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalRevistas.setForeground(new java.awt.Color(255, 0, 0));
        campoTotalRevistas.setEnabled(false);
        campoTotalRevistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTotalRevistasActionPerformed(evt);
            }
        });

        campoTotalOfertas.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalOfertas.setForeground(new java.awt.Color(0, 153, 51));
        campoTotalOfertas.setText("R$");
        campoTotalOfertas.setEnabled(false);
        campoTotalOfertas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoTotalOfertasActionPerformed(evt);
            }
        });

        campoTotalOfertMis.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        campoTotalOfertMis.setForeground(new java.awt.Color(0, 153, 102));
        campoTotalOfertMis.setText("R$");
        campoTotalOfertMis.setEnabled(false);

        txtPergunta.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPergunta.setForeground(new java.awt.Color(102, 102, 102));
        txtPergunta.setText("Qual data quer ver o total geral?");

        campoDia.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        campoDia.setForeground(new java.awt.Color(153, 153, 153));
        campoDia.setText("Dia");
        campoDia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoDiaKeyPressed(evt);
            }
        });

        botaoPesquisar.setBackground(new java.awt.Color(255, 255, 255));
        botaoPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/lupaExcel.png"))); // NOI18N
        botaoPesquisar.setToolTipText("Pesquisar");
        botaoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisarActionPerformed(evt);
            }
        });
        botaoPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                botaoPesquisarKeyPressed(evt);
            }
        });

        campoMes.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        campoMes.setForeground(new java.awt.Color(153, 153, 153));
        campoMes.setText("Mês");
        campoMes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoMesKeyPressed(evt);
            }
        });

        campoAno.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        campoAno.setForeground(new java.awt.Color(153, 153, 153));
        campoAno.setText("Ano");
        campoAno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoAnoKeyPressed(evt);
            }
        });

        txtPerguntaDept.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        txtPerguntaDept.setForeground(new java.awt.Color(102, 102, 102));
        txtPerguntaDept.setText("Qual departamento quer ver o Sub-Total?");

        campoDept.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        campoDept.setToolTipText("");
        campoDept.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoDeptKeyPressed(evt);
            }
        });

        botaoImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/imprimirExcel.png"))); // NOI18N
        botaoImprimir.setToolTipText("Imprimir");
        botaoImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoImprimirActionPerformed(evt);
            }
        });

        botaoEnviar.setBackground(new java.awt.Color(255, 204, 0));
        botaoEnviar.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        botaoEnviar.setText("Enviar dados");
        botaoEnviar.setToolTipText("Enviar Dados");
        botaoEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoEnviarActionPerformed(evt);
            }
        });

        textObs.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        textObs.setForeground(new java.awt.Color(204, 0, 0));
        textObs.setText("Obs: Depois que pesquisar os totais pela data, enviar os dados para poder imprimir pela data de registro de hoje. ");

        javax.swing.GroupLayout PainelCinzaLayout = new javax.swing.GroupLayout(PainelCinza);
        PainelCinza.setLayout(PainelCinzaLayout);
        PainelCinzaLayout.setHorizontalGroup(
            PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelCinzaLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PainelCinzaLayout.createSequentialGroup()
                        .addComponent(textObs, javax.swing.GroupLayout.PREFERRED_SIZE, 643, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(PainelCinzaLayout.createSequentialGroup()
                        .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PainelCinzaLayout.createSequentialGroup()
                                .addComponent(txtPresentVisitante)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoTotalPresVis, javax.swing.GroupLayout.DEFAULT_SIZE, 139, Short.MAX_VALUE))
                            .addGroup(PainelCinzaLayout.createSequentialGroup()
                                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPresentes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtMatriculados, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(campoTotalMatriculados)
                                    .addComponent(campoTotalPresentes, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PainelCinzaLayout.createSequentialGroup()
                                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtBiblias, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtRevistas, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(campoTotalBiblias, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                                    .addComponent(campoTotalRevistas))))
                        .addGap(18, 18, 18)
                        .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PainelCinzaLayout.createSequentialGroup()
                                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtAusentes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtVisitantes, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE))
                                    .addComponent(txtOfertas, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(campoTotalAusentes, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(campoTotalOfertas, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(campoTotalVisitantes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))))
                            .addGroup(PainelCinzaLayout.createSequentialGroup()
                                .addComponent(txtOfertMissoes, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoTotalOfertMis, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(PainelCinzaLayout.createSequentialGroup()
                        .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPerguntaDept, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPergunta))
                        .addGap(18, 18, 18)
                        .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PainelCinzaLayout.createSequentialGroup()
                                .addComponent(campoDia, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(campoMes, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(campoAno, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(botaoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(campoDept, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botaoEnviar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botaoImprimir)
                        .addGap(26, 26, 26))))
        );
        PainelCinzaLayout.setVerticalGroup(
            PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PainelCinzaLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMatriculados, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalMatriculados, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtAusentes, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalAusentes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPresentes, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalPresentes, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtVisitantes, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalVisitantes, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPresentVisitante, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalPresVis, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtBiblias, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalBiblias, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOfertas, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalOfertas, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtRevistas, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalRevistas, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtOfertMissoes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(campoTotalOfertMis, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(botaoEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PainelCinzaLayout.createSequentialGroup()
                        .addComponent(textObs, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPerguntaDept, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(campoDept, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PainelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(campoDia, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(campoMes, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(campoAno, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botaoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(botaoImprimir, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(38, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PainelAmarelo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PainelCinza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(PainelAmarelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PainelCinza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setBounds(0, 0, 968, 666);
    }// </editor-fold>//GEN-END:initComponents

    private void campoTotalAusentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTotalAusentesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTotalAusentesActionPerformed

    private void campoTotalPresentesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTotalPresentesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTotalPresentesActionPerformed

    private void campoTotalRevistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTotalRevistasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTotalRevistasActionPerformed

    private void campoTotalOfertasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoTotalOfertasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoTotalOfertasActionPerformed

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        // chamando o método consultar.
        String comparar = campoDept.getText();
        String texto = "Geral";
        String texto2 = "geral";
        if(comparar.equals(texto)|| comparar.equals(texto2)){
            consultar();
        }else{
            subTotalConsulta();
    }
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void botaoEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoEnviarActionPerformed
        // Botão para enviar as informações que estão nos campos.
        enviarDados();
    }//GEN-LAST:event_botaoEnviarActionPerformed

    private void botaoImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoImprimirActionPerformed
        // Botão para imprimir as informações dos totais enviados hoje
        imprimir_data();
    }//GEN-LAST:event_botaoImprimirActionPerformed

    private void campoDeptKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDeptKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoDia.requestFocus();
        }
    }//GEN-LAST:event_campoDeptKeyPressed

    private void campoDiaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDiaKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoMes.requestFocus();
        }
    }//GEN-LAST:event_campoDiaKeyPressed

    private void campoMesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoMesKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoAno.requestFocus();
        }
    }//GEN-LAST:event_campoMesKeyPressed

    private void campoAnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoAnoKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            botaoPesquisar.requestFocus();
        }
    }//GEN-LAST:event_campoAnoKeyPressed

    private void botaoPesquisarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_botaoPesquisarKeyPressed
        // Colocando atalho enter para pesquisar com dois cliques.
        if(evt.getKeyCode() == evt.VK_ENTER){
        // chamando o método consultar.
        String comparar = campoDept.getText();
        String texto = "Geral";
        String texto2 = "geral";
        if(comparar.equals(texto)|| comparar.equals(texto2)){
            consultar();
        }else{
            subTotalConsulta();
    }
    }
    }//GEN-LAST:event_botaoPesquisarKeyPressed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TelaInformacao().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PainelAmarelo;
    private javax.swing.JPanel PainelCinza;
    private javax.swing.JButton botaoEnviar;
    private javax.swing.JButton botaoImprimir;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JTextField campoAno;
    private javax.swing.JTextField campoDept;
    private javax.swing.JTextField campoDia;
    private javax.swing.JTextField campoMes;
    public javax.swing.JTextField campoTotalAusentes;
    public javax.swing.JTextField campoTotalBiblias;
    public javax.swing.JTextField campoTotalMatriculados;
    public javax.swing.JTextField campoTotalOfertMis;
    public javax.swing.JTextField campoTotalOfertas;
    public javax.swing.JTextField campoTotalPresVis;
    public javax.swing.JTextField campoTotalPresentes;
    public javax.swing.JTextField campoTotalRevistas;
    public javax.swing.JTextField campoTotalVisitantes;
    private javax.swing.JLabel textObs;
    private javax.swing.JLabel txtAusentes;
    private javax.swing.JLabel txtBiblias;
    private javax.swing.JLabel txtMatriculados;
    private javax.swing.JLabel txtOfertMissoes;
    private javax.swing.JLabel txtOfertas;
    private javax.swing.JLabel txtPergunta;
    private javax.swing.JLabel txtPerguntaDept;
    private javax.swing.JLabel txtPresentVisitante;
    private javax.swing.JLabel txtPresentes;
    private javax.swing.JLabel txtRevistas;
    private javax.swing.JLabel txtTitulo;
    private javax.swing.JLabel txtVisitantes;
    // End of variables declaration//GEN-END:variables

}
