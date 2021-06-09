/*Tela de cadastro de membros da igreja.
 */
package br.com.cadastroCohab.telas;

//Importar pacote java MySQL.
import java.sql.*;
import br.com.cadastroCohab.dal.ModuloConexao;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import javax.swing.JOptionPane;
//A linha abaixo importa recursos da biblioteca rs2xml.jar
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Fabiana de Albuquerque Silva.
 */
public class TelaMembros extends javax.swing.JInternalFrame {

    Connection conexao = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    

    /**
     * Creates new form TelaMembros
     */
    public TelaMembros() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    //método para adicionar membros.
    private void adicionar() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataFormatada = simpleDateFormat.format(this.campoData.getDate());
        
        String sql = "insert into tbmembros(nomeMembro,endMenbro,foneMembro,classeMembro,funcaoMembro,paiMembro,maeMembro,profissaoMembro,grauMembro,dataMembro,membro,tipoMembro) values(?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, campoNome.getText());
            pst.setString(2, campoEndereco.getText());
            pst.setString(3, campoFone.getText());
            pst.setString(4, campoClasse.getText());
            pst.setString(5, campoFuncao.getSelectedItem().toString());
            pst.setString(6, campoNomePai.getText());
            pst.setString(7, campoNomeMae.getText());
            pst.setString(8, campoProfissao.getText());
            pst.setString(9, campoGrau.getText());
            pst.setString(10, dataFormatada);
            pst.setString(11, comboBoxMembro.getSelectedItem().toString());
            pst.setString(12, comboBoxTipo.getSelectedItem().toString());
            //Validação dos campos obrigatórios.
            if (((((campoNome.getText().isEmpty()) || (campoEndereco.getText().isEmpty()) || (campoFone.getText().isEmpty()) || (campoClasse.getText().isEmpty()) || (dataFormatada.isEmpty()))))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");

            } else {

                //A linha abaixo atualiza a tabela usuario com os dados do formulário.
                // A estrutura abaixo é usada para confirmar a inserção dos dados na tabela.
                int adicionado = pst.executeUpdate();
                //A linha abaixo serve de apoio.
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Membro adicionado com sucesso!");
                    // as linhas abaixo "limpam" os campos.
                    campoNome.setText(null);
                    campoEndereco.setText(null);
                    campoFone.setText(null);
                    campoClasse.setText(null);
                    campoNomePai.setText(null);
                    campoNomeMae.setText(null);
                    campoData.setCalendar(null);
                    campoProfissao.setText(null);
                    campoGrau.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Metodo para pesquisar clientes pelo nome com filtro.
    private void pesquisar_membro() {
        String sql = "select * from tbmembros where nomeMembro like ?";
        try {
            pst = conexao.prepareStatement(sql);
            //passando o conteúdo da caixa de pesquisa para o ?.
            //atenção ao "%" - continuação da String sql.
            pst.setString(1, campoPesquisar.getText() + "%");
            rs = pst.executeQuery();
            //A linha abaixo usa a biblioteca rs2xml.jar para preencher a tabela.
            tabelaMembros.setModel(DbUtils.resultSetToTableModel(rs));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Método para setar os campos do formulário com o conteúdo da tabela.
    public void setar_campos() {
        int setar = tabelaMembros.getSelectedRow();
        campoId.setText(tabelaMembros.getModel().getValueAt(setar, 0).toString());
        campoNome.setText(tabelaMembros.getModel().getValueAt(setar, 1).toString());
        campoEndereco.setText(tabelaMembros.getModel().getValueAt(setar, 2).toString());
        campoFone.setText(tabelaMembros.getModel().getValueAt(setar, 3).toString());
        campoClasse.setText(tabelaMembros.getModel().getValueAt(setar, 4).toString());
        campoFuncao.setSelectedItem(tabelaMembros.getModel().getValueAt(setar, 5).toString());
        campoNomePai.setText(tabelaMembros.getModel().getValueAt(setar, 6).toString());
        campoNomeMae.setText(tabelaMembros.getModel().getValueAt(setar, 7).toString());
        campoProfissao.setText(tabelaMembros.getModel().getValueAt(setar, 8).toString());
        campoGrau.setText(tabelaMembros.getModel().getValueAt(setar, 9).toString());
        dateSetar.setText(tabelaMembros.getModel().getValueAt(setar, 10).toString()); //Mostrar data de nascimento do membro no campo dateSetar.
        comboBoxMembro.setSelectedItem(tabelaMembros.getModel().getValueAt(setar, 11).toString());
        comboBoxTipo.setSelectedItem(tabelaMembros.getModel().getValueAt(setar, 12).toString());
        
        //A linha abaixo desabilita o botão adicionar enquanto seta para editar.
        botaoSalvar.setEnabled(false);

    }

    //Criando o método para alterar dados dos membros.
    private void alterar() {
        
        String sql = "update tbmembros set nomeMembro=?,endMenbro=?,foneMembro=?,classeMembro=?,funcaoMembro=?,paiMembro=?,maeMembro=?,profissaoMembro=?,grauMembro=?,dataMembro=?,membro=?,tipoMembro=? where idMembro=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, campoNome.getText());
            pst.setString(2, campoEndereco.getText());
            pst.setString(3, campoFone.getText());
            pst.setString(4, campoClasse.getText());
            pst.setString(5, campoFuncao.getSelectedItem().toString());
            pst.setString(6, campoNomePai.getText());
            pst.setString(7, campoNomeMae.getText());
            pst.setString(8, campoProfissao.getText());
            pst.setString(9, campoGrau.getText());
            pst.setString(10, dateSetar.getText());
            pst.setString(11, comboBoxMembro.getSelectedItem().toString());
            pst.setString(12, comboBoxTipo.getSelectedItem().toString());
            pst.setString(13, campoId.getText());
            if (((((campoNome.getText().isEmpty()) || (campoEndereco.getText().isEmpty()) || (campoFone.getText().isEmpty()) || (campoClasse.getText().isEmpty()) || (dateSetar.getText().isEmpty()))))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");

            } else {

                //A linha abaixo atualiza a tabela membros com os dados do formulário.
                // A estrutura abaixo é usada para confirmar a alteração dos dados na tabela.
                int adicionado = pst.executeUpdate();
                //A linha abaixo serve de apoio.
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do membro alterados com sucesso!");
                    // as linhas abaixo "limpam" os campos.
                    campoId.setText(null);
                    campoNome.setText(null);
                    campoEndereco.setText(null);
                    campoFone.setText(null);
                    campoClasse.setText(null);
                    campoNomePai.setText(null);
                    campoNomeMae.setText(null);
                    campoData.setCalendar(null);
                    campoProfissao.setText(null);
                    campoGrau.setText(null);
                    dateSetar.setText(null);
                    
                    //Reabilita o botão salvar.
                    botaoSalvar.setEnabled(true);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }
    
     //Método responsável pela remoção de membros.
    private void remover(){
        //A estrutura abaixo confirma a remoção de membros.
        int confirma=JOptionPane.showConfirmDialog(null,"Tem certeza que deseja remover este membro?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma==JOptionPane.YES_OPTION){
            String sql="delete from tbmembros where idMembro=?";
            try {
                pst=conexao.prepareStatement(sql);
                pst.setString(1,campoId.getText());
                int apagado = pst.executeUpdate();
                if(apagado>0){
                    JOptionPane.showMessageDialog(null,"Membro removido com sucesso!");
                    campoId.setText(null);
                    campoNome.setText(null);
                    campoEndereco.setText(null);
                    campoFone.setText(null);
                    campoClasse.setText(null);
                    campoNomePai.setText(null);
                    campoNomeMae.setText(null);
                    campoData.setCalendar(null);
                    campoProfissao.setText(null);
                    campoGrau.setText(null);
                    dateSetar.setText(null);
                    
                    //Reabilita o botão salvar.
                    botaoSalvar.setEnabled(true);
                }
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

        jComboBox1 = new javax.swing.JComboBox<>();
        painelCinza = new javax.swing.JPanel();
        nome1 = new javax.swing.JLabel();
        nome2 = new javax.swing.JLabel();
        painelAzul = new javax.swing.JPanel();
        nome3 = new javax.swing.JLabel();
        nome4 = new javax.swing.JLabel();
        nome5 = new javax.swing.JLabel();
        nome6 = new javax.swing.JLabel();
        nome7 = new javax.swing.JLabel();
        nome8 = new javax.swing.JLabel();
        nome9 = new javax.swing.JLabel();
        nome10 = new javax.swing.JLabel();
        nome12 = new javax.swing.JLabel();
        nome13 = new javax.swing.JLabel();
        nome14 = new javax.swing.JLabel();
        comboBoxMembro = new javax.swing.JComboBox<>();
        campoNome = new javax.swing.JTextField();
        campoEndereco = new javax.swing.JTextField();
        campoFone = new javax.swing.JTextField();
        campoClasse = new javax.swing.JTextField();
        campoNomePai = new javax.swing.JTextField();
        campoNomeMae = new javax.swing.JTextField();
        campoProfissao = new javax.swing.JTextField();
        campoGrau = new javax.swing.JTextField();
        botaoSalvar = new javax.swing.JButton();
        botaoEditar = new javax.swing.JButton();
        botaoDeletar = new javax.swing.JButton();
        campoPesquisar = new javax.swing.JTextField();
        pesquisarMembro = new javax.swing.JLabel();
        Img = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelaMembros = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        comboBoxTipo = new javax.swing.JComboBox<>();
        txtId = new javax.swing.JLabel();
        campoId = new javax.swing.JTextField();
        imprimirMembros = new javax.swing.JButton();
        campoData = new com.toedter.calendar.JDateChooser();
        dateSetar = new javax.swing.JTextField();
        TextAlert = new javax.swing.JLabel();
        campoFuncao = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        btnTextoClasseImprimir = new javax.swing.JLabel();
        ComboBoxClasseImpressao = new javax.swing.JComboBox<>();
        btnImprimirClasse = new javax.swing.JButton();

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Cadastro de Membros");
        setPreferredSize(new java.awt.Dimension(968, 666));

        painelCinza.setBackground(new java.awt.Color(92, 92, 172));

        nome1.setFont(new java.awt.Font("Century", 1, 36)); // NOI18N
        nome1.setForeground(new java.awt.Color(255, 255, 255));
        nome1.setText("Cadastro de Membros");

        nome2.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        nome2.setText("* Campos Obrigatórios");

        javax.swing.GroupLayout painelCinzaLayout = new javax.swing.GroupLayout(painelCinza);
        painelCinza.setLayout(painelCinzaLayout);
        painelCinzaLayout.setHorizontalGroup(
            painelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelCinzaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nome1, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(79, 79, 79)
                .addComponent(nome2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(262, 262, 262))
        );
        painelCinzaLayout.setVerticalGroup(
            painelCinzaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelCinzaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(nome1, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelCinzaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(nome2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        painelAzul.setBackground(new java.awt.Color(204, 204, 204));

        nome3.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome3.setText("*Nome completo:");

        nome4.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome4.setText("*Endereço:");

        nome5.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome5.setText("*Telefone:");

        nome6.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome6.setText("*Classe:");

        nome7.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome7.setText("Função:");

        nome8.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome8.setText("Nome do pai:");

        nome9.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome9.setText("Nome da mãe:");

        nome10.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome10.setText("Profissão:");

        nome12.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome12.setText("Grau de instrução:");

        nome13.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome13.setText("*Data de nascimento:");

        nome14.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome14.setText("*Membro?");

        comboBoxMembro.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxMembro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sim", "Não" }));
        comboBoxMembro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxMembroActionPerformed(evt);
            }
        });

        campoNome.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoNomeKeyPressed(evt);
            }
        });

        campoEndereco.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoEndereco.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoEnderecoKeyPressed(evt);
            }
        });

        campoFone.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoFone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoFoneKeyPressed(evt);
            }
        });

        campoClasse.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoClasse.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoClasseKeyPressed(evt);
            }
        });

        campoNomePai.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoNomePai.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoNomePaiKeyPressed(evt);
            }
        });

        campoNomeMae.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoNomeMae.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoNomeMaeKeyPressed(evt);
            }
        });

        campoProfissao.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoProfissao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoProfissaoKeyPressed(evt);
            }
        });

        campoGrau.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoGrau.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoGrauKeyPressed(evt);
            }
        });

        botaoSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/Save.png"))); // NOI18N
        botaoSalvar.setToolTipText("Salvar");
        botaoSalvar.setEnabled(false);
        botaoSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoSalvarActionPerformed(evt);
            }
        });

        botaoEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/Editar.png"))); // NOI18N
        botaoEditar.setToolTipText("Editar");
        botaoEditar.setEnabled(false);
        botaoEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoEditarActionPerformed(evt);
            }
        });

        botaoDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/lixeira.png"))); // NOI18N
        botaoDeletar.setToolTipText("Deletar");
        botaoDeletar.setEnabled(false);
        botaoDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoDeletarActionPerformed(evt);
            }
        });

        campoPesquisar.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoPesquisarActionPerformed(evt);
            }
        });
        campoPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                campoPesquisarKeyReleased(evt);
            }
        });

        pesquisarMembro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/pesquisar.png"))); // NOI18N

        tabelaMembros.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        tabelaMembros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tabelaMembros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelaMembrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelaMembros);

        jLabel2.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        jLabel2.setText("*O membro é:");

        comboBoxTipo.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Novo(a)  convertido(a)", "Aluno(a)", "Professor(a)", "Coordenador(a) de Departamento", "Secretário(a)", "Tesoureiro(a)", "Auxiliar", "Diacono", "Presbítero", "Superintendente", "Pastor", "Não possui" }));

        txtId.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        txtId.setText("Id:");

        campoId.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoId.setEnabled(false);

        imprimirMembros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/imprimirExcel.png"))); // NOI18N
        imprimirMembros.setToolTipText("Imprimir ficha");
        imprimirMembros.setEnabled(false);
        imprimirMembros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imprimirMembrosActionPerformed(evt);
            }
        });

        campoData.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        campoData.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoDataKeyPressed(evt);
            }
        });

        TextAlert.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        TextAlert.setText("Alterar a data: ");

        campoFuncao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        campoFuncao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Aluno(a)", "Professor(a)", "Coordenador(a) de Departamento", "Secretário(a)", "Tesoureiro(a)", "Não possui outra função" }));
        campoFuncao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoFuncaoKeyPressed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnTextoClasseImprimir.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        btnTextoClasseImprimir.setText("Digite a classe que quer imprimir:");

        ComboBoxClasseImpressao.setBackground(new java.awt.Color(102, 204, 255));
        ComboBoxClasseImpressao.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        ComboBoxClasseImpressao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Arão", "Berçário", "Betel", "Boáz", "Daniel", "Davi", "Débora", "Dorcas", "Ester", "Ezequiel", "Filadefia", "Gideão", "Isaac", "Isaias", "Jeezquel", "José do Egito", "Levitas", "Lídia", "Miriã", "Moisés", "Neemias", "Noemi", "Oséias", "Raquel", "Rebeca" }));

        btnImprimirClasse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/imprimirClasse.png"))); // NOI18N
        btnImprimirClasse.setToolTipText("Imprimir de uma classe");
        btnImprimirClasse.setEnabled(false);
        btnImprimirClasse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirClasseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnTextoClasseImprimir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ComboBoxClasseImpressao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(btnImprimirClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnTextoClasseImprimir)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ComboBoxClasseImpressao, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnImprimirClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(35, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout painelAzulLayout = new javax.swing.GroupLayout(painelAzul);
        painelAzul.setLayout(painelAzulLayout);
        painelAzulLayout.setHorizontalGroup(
            painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelAzulLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelAzulLayout.createSequentialGroup()
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nome12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nome10, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1179, 1179, 1179))
                    .addGroup(painelAzulLayout.createSequentialGroup()
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAzulLayout.createSequentialGroup()
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nome4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nome5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nome6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nome7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nome8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(nome9, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
                                    .addComponent(botaoSalvar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(painelAzulLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addComponent(campoFuncao, javax.swing.GroupLayout.Alignment.LEADING, 0, 265, Short.MAX_VALUE)
                                                .addComponent(campoFone, javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(campoClasse, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelAzulLayout.createSequentialGroup()
                                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(painelAzulLayout.createSequentialGroup()
                                                        .addComponent(botaoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(botaoDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(campoProfissao, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                            .addComponent(campoNomePai, javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(campoNomeMae, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
                                                        .addComponent(campoGrau, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(31, 31, 31)
                                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(painelAzulLayout.createSequentialGroup()
                                                        .addComponent(campoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(pesquisarMembro))
                                                    .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(painelAzulLayout.createSequentialGroup()
                                                            .addComponent(imprimirMembros, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addGap(92, 92, 92)
                                                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(painelAzulLayout.createSequentialGroup()
                                        .addGap(4, 4, 4)
                                        .addComponent(campoEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(36, 36, 36)
                                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(painelAzulLayout.createSequentialGroup()
                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comboBoxTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(painelAzulLayout.createSequentialGroup()
                                                .addComponent(nome14, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addGap(10, 10, 10))))
                            .addGroup(painelAzulLayout.createSequentialGroup()
                                .addComponent(txtId)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoId, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(nome3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(campoNome, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(36, 36, 36)
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nome13)
                                    .addComponent(TextAlert))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboBoxMembro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateSetar, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(campoData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(10, 10, 10)))
                        .addComponent(Img, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(243, 243, 243))))
        );
        painelAzulLayout.setVerticalGroup(
            painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelAzulLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Img, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(painelAzulLayout.createSequentialGroup()
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtId, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(campoId, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nome3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(campoNome, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(nome13, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(campoData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAzulLayout.createSequentialGroup()
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nome4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(campoEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nome5, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(campoFone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1)
                                .addComponent(nome6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelAzulLayout.createSequentialGroup()
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(dateSetar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextAlert, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nome14, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBoxMembro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(campoClasse, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBoxTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelAzulLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nome7, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(campoFuncao))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nome8, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(campoNomePai, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nome9, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
                            .addComponent(campoNomeMae))
                        .addGap(6, 6, 6)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nome10, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(campoProfissao, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nome12, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(campoGrau, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botaoEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(botaoSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botaoDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(painelAzulLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAzulLayout.createSequentialGroup()
                                .addGap(0, 4, Short.MAX_VALUE)
                                .addComponent(campoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(pesquisarMembro))
                        .addGroup(painelAzulLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAzulLayout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelAzulLayout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addComponent(imprimirMembros, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(23, 23, 23))))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelCinza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(169, 169, 169))
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelAzul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(painelCinza, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(painelAzul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(0, 0, 968, 666);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoSalvarActionPerformed
        // Método para adicionar membros.
        adicionar();
    }//GEN-LAST:event_botaoSalvarActionPerformed

    private void campoPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoPesquisarKeyReleased
        // chamar o método pesquisar membros.
        pesquisar_membro();
    }//GEN-LAST:event_campoPesquisarKeyReleased

    private void tabelaMembrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelaMembrosMouseClicked
        // Evento que será usado para setar os campos da tabela (clicando com o mouse)
        setar_campos();
    }//GEN-LAST:event_tabelaMembrosMouseClicked

    private void botaoEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoEditarActionPerformed
        // Método para alterar membros.
        alterar();
    }//GEN-LAST:event_botaoEditarActionPerformed

    private void botaoDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoDeletarActionPerformed
        // Chamando o método para remover o membro.
        remover();
    }//GEN-LAST:event_botaoDeletarActionPerformed

    private void campoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoPesquisarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_campoPesquisarActionPerformed

    private void imprimirMembrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imprimirMembrosActionPerformed
        // Gerando uma ficha de todos os membros cadastrados.
        int confirma = JOptionPane.showConfirmDialog(null,"Confirma a impressão desta ficha de membros cadastrados?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            //Imprimindo relatório com o framework JasperReports.
            try{
                //Usando a classe JasperPrint para preparar a impressão de um relatório.
               JasperPrint print = JasperFillManager.fillReport("C:/reports/fichaMembro.jasper",null,conexao);
               //A linha abaixo exibe a ficha de membros através da classe JasperViewer.
               JasperViewer.viewReport(print,false);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, e);
            }
        }
        
    }//GEN-LAST:event_imprimirMembrosActionPerformed

    private void comboBoxMembroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxMembroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxMembroActionPerformed

    private void btnImprimirClasseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirClasseActionPerformed
        // Gerando uma ficha de classe com filtro.
        int confirma = JOptionPane.showConfirmDialog(null,"Confirma a impressão da ficha desta classe?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma == JOptionPane.YES_OPTION){
            //Imprimindo relatório com o framework JasperReports.
            //SQL para fazer o filtro da classe escolhida pelo usuário.
            String sql = ("select * from tbmembros where classeMembro=?");
            try{
                //Conexão do sql sendo passada para a variável pst.
                 pst = conexao.prepareStatement(sql);
                 //Passando o campo com o dado escolhido pelo usuário.
                 pst.setString(1, ComboBoxClasseImpressao.getSelectedItem().toString());;
                 //Executando o sql passando do pst para o rs, que possui o ResultSet.
                 rs = pst.executeQuery();
                 //Passando o sql dentro do rs com o resultset para poder enviar ao relatório.
                 JRResultSetDataSource jrRS = new JRResultSetDataSource(rs);
                //Usando a classe JasperPrint para preparar a impressão de um relatório.
               JasperPrint print = JasperFillManager.fillReport("C:/reports/fichaClasseMembro.jasper",new HashMap(), jrRS);
               //A linha abaixo exibe a ficha de membros através da classe JasperViewer.
               JasperViewer.viewReport(print,false);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null,"deu erro ="+e);
            }
        }
    }//GEN-LAST:event_btnImprimirClasseActionPerformed

    private void campoNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoEndereco.requestFocus();
        }
    }//GEN-LAST:event_campoNomeKeyPressed

    private void campoEnderecoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoEnderecoKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoFone.requestFocus();
        }
    }//GEN-LAST:event_campoEnderecoKeyPressed

    private void campoFoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoFoneKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoClasse.requestFocus();
        }
    }//GEN-LAST:event_campoFoneKeyPressed

    private void campoClasseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoClasseKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoFuncao.requestFocus();
        }
    }//GEN-LAST:event_campoClasseKeyPressed

    private void campoFuncaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoFuncaoKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoNomePai.requestFocus();
        }
    }//GEN-LAST:event_campoFuncaoKeyPressed

    private void campoNomePaiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomePaiKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoNomeMae.requestFocus();
        }
    }//GEN-LAST:event_campoNomePaiKeyPressed

    private void campoNomeMaeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeMaeKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoProfissao.requestFocus();
        }
    }//GEN-LAST:event_campoNomeMaeKeyPressed

    private void campoProfissaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoProfissaoKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoGrau.requestFocus();
        }
    }//GEN-LAST:event_campoProfissaoKeyPressed

    private void campoGrauKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoGrauKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoData.requestFocus();
        }
    }//GEN-LAST:event_campoGrauKeyPressed

    private void campoDataKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoDataKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            comboBoxMembro.requestFocus();
        }
    }//GEN-LAST:event_campoDataKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxClasseImpressao;
    private javax.swing.JLabel Img;
    private javax.swing.JLabel TextAlert;
    public static javax.swing.JButton botaoDeletar;
    public static javax.swing.JButton botaoEditar;
    public static javax.swing.JButton botaoSalvar;
    public static javax.swing.JButton btnImprimirClasse;
    private javax.swing.JLabel btnTextoClasseImprimir;
    private javax.swing.JTextField campoClasse;
    private com.toedter.calendar.JDateChooser campoData;
    private javax.swing.JTextField campoEndereco;
    private javax.swing.JTextField campoFone;
    private javax.swing.JComboBox<String> campoFuncao;
    private javax.swing.JTextField campoGrau;
    private javax.swing.JTextField campoId;
    private javax.swing.JTextField campoNome;
    private javax.swing.JTextField campoNomeMae;
    private javax.swing.JTextField campoNomePai;
    private javax.swing.JTextField campoPesquisar;
    private javax.swing.JTextField campoProfissao;
    private javax.swing.JComboBox<String> comboBoxMembro;
    private javax.swing.JComboBox<String> comboBoxTipo;
    private javax.swing.JTextField dateSetar;
    public static javax.swing.JButton imprimirMembros;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel nome1;
    private javax.swing.JLabel nome10;
    private javax.swing.JLabel nome12;
    private javax.swing.JLabel nome13;
    private javax.swing.JLabel nome14;
    private javax.swing.JLabel nome2;
    private javax.swing.JLabel nome3;
    private javax.swing.JLabel nome4;
    private javax.swing.JLabel nome5;
    private javax.swing.JLabel nome6;
    private javax.swing.JLabel nome7;
    private javax.swing.JLabel nome8;
    private javax.swing.JLabel nome9;
    private javax.swing.JPanel painelAzul;
    private javax.swing.JPanel painelCinza;
    private javax.swing.JLabel pesquisarMembro;
    private javax.swing.JTable tabelaMembros;
    private javax.swing.JLabel txtId;
    // End of variables declaration//GEN-END:variables
}
