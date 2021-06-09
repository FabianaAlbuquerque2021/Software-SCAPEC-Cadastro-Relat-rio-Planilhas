/* Tela de cadastro de usuários no sistema.
 */
package br.com.cadastroCohab.telas;

/**
 *
 * @author Fabiana de Albuquerque Silva
 */
import java.sql.*;
import br.com.cadastroCohab.dal.ModuloConexao;
import static br.com.cadastroCohab.telas.TelaPrincipal.nomeUsuario;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.security.MessageDigest;

public class TelaUsuario extends javax.swing.JInternalFrame {

    //Variáveis de conexão do Dal.
    Connection conexao = null;
    //Variáveis especiais de conexão.
    PreparedStatement pst = null;
    ResultSet rs = null;

    public TelaUsuario() {
        initComponents();
        conexao = ModuloConexao.conector();
    }

    //metodo para consultar usuários.
    private void consultar() {
        String sql = "select * from tbusuarios where idusuario=?";
        try {
            pst = conexao.prepareStatement(sql);
            pst.setString(1, campoId.getText());
            rs = pst.executeQuery();
            
            if (rs.next()) {
                //Verifica também se o nome do usuário bate com o do puxado pelo id do banco.
                //Se for ele permite que os campos seja enabled true, pode modificar os campos.
                //Senão não pode modificar esses campos. Apenas o Administrador tem permissão de modificar em qualquer perfil.
                String nomePermitido=rs.getString(2);
                //A linha abaixo contem o conteudo do campo perfil na tabela tbusuarios.
                if(nomeUsuario.getText().equals("Administrador")){
                campoSenha.setEnabled(true);
                campoLogin.setEnabled(true);
                campoNome.setEnabled(true);
                campoTelefone.setEnabled(true);
                comboBoxPerfil.setEnabled(true);
                
                //Colocando os dados nos campos
                campoNome.setText(rs.getString(2));
                campoTelefone.setText(rs.getString(3));
                campoLogin.setText(rs.getString(4));
                campoSenha.setText(rs.getString(5));
                //A linha abaixo se refere ao combobox.
                comboBoxPerfil.setSelectedItem(rs.getString(6));
                
                }else if (nomePermitido.equals(nomeUsuario.getText())){
                campoSenha.setEnabled(true);
                campoSenha.setText(null);
                campoLogin.setEnabled(true);
                campoNome.setEnabled(true);
                campoTelefone.setEnabled(true);
                comboBoxPerfil.setEnabled(true);
                //Colocando os dados nos campos
                campoNome.setText(rs.getString(2));
                campoTelefone.setText(rs.getString(3));
                campoLogin.setText(rs.getString(4));
                //campoSenha.setText(rs.getString(5));
                //A linha abaixo se refere ao combobox.
                comboBoxPerfil.setSelectedItem(rs.getString(6));
                }else{
                     campoSenha.setEnabled(false);
                     campoLogin.setEnabled(false);
                     campoNome.setEnabled(false);
                     campoTelefone.setEnabled(false);
                     comboBoxPerfil.setEnabled(false);
                     //Colocando os dados nos campos.
                     campoNome.setText(rs.getString(2));
                     campoTelefone.setText(rs.getString(3));
                     campoLogin.setText(rs.getString(4));
                    // campoSenha.setText(rs.getString(5));
                     //A linha abaixo se refere ao combobox.
                     comboBoxPerfil.setSelectedItem(rs.getString(6));
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuário não cadastrado!");
                // as linhas abaixo "limpam" os campos.
                campoNome.setText(null);
                campoTelefone.setText(null);
                campoLogin.setText(null);
                campoSenha.setText(null);
                //Liberar os campos para serem preenchidos
                campoSenha.setEnabled(true);
                campoLogin.setEnabled(true);
                campoNome.setEnabled(true);
                campoTelefone.setEnabled(true);
                comboBoxPerfil.setEnabled(true);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    //método para adicionar usuários.
    private void adicionar() {
        String sql = "insert into tbusuarios(idusuario,usuario,fone,login,senha,perfil) values(?,?,?,?,?,?)";
        String senha = campoSenha.getText(); // Criando Hash para proteger senhas
        try {
            //Para ser criptografado usando o algoritmo SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));
            //Array de Bytes para pegar todos os bytes e converter para string. Usando a classe string builder.
            StringBuilder sb = new StringBuilder();
            //Máscara de formatação para que não tenha números negativos.
            for(byte b : messageDigest){
                sb.append(String.format("%02X", 0xFF & b));
            }
            //Essa variável senhaHex já vai pegar a senha com hash, convertida para string e concatenada.
            String senhaHex = sb.toString();
            
            
            pst = conexao.prepareStatement(sql);
            pst.setString(1, campoId.getText());
            pst.setString(2, campoNome.getText());
            pst.setString(3, campoTelefone.getText());
            pst.setString(4, campoLogin.getText());
            pst.setString(5, senhaHex); //Senha com Hash para proteção no BD.
            pst.setString(6, comboBoxPerfil.getSelectedItem().toString());
            //Validação dos campos obrigatórios.
            if ((((campoId.getText().isEmpty())||(campoNome.getText().isEmpty())||(campoLogin.getText().isEmpty())||(campoSenha.getText().isEmpty())))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios!");

            } else {

                //A linha abaixo atualiza a tabela usuario com os dados do formulário.
                // A estrutura abaixo é usada para confirmar a inserção dos dados na tabela.
                int adicionado = pst.executeUpdate();
                //A linha abaixo serve de apoio.
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Usuário adicionado com sucesso!");
                    // as linhas abaixo "limpam" os campos.
                    campoId.setText(null);
                    campoNome.setText(null);
                    campoTelefone.setText(null);
                    campoLogin.setText(null);
                    campoSenha.setText(null);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    
    //Criando o método para alterar dados do usuário.
    private void alterar(){
        String sql="update tbusuarios set usuario=?,fone=?,login=?,senha=?,perfil=? where idusuario=?";
        String senha = campoSenha.getText(); // Criando Hash para proteger senhas
        try {
            //Para ser criptografado usando o algoritmo SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte messageDigest[] = md.digest(senha.getBytes("UTF-8"));
            //Array de Bytes para pegar todos os bytes e converter para string. Usando a classe string builder.
            StringBuilder sb = new StringBuilder();
            //Máscara de formatação para que não tenha números negativos.
            for(byte b : messageDigest){
                sb.append(String.format("%02X", 0xFF & b));
            }
            //Essa variável senhaHex já vai pegar a senha com hash, convertida para string e concatenada.
            String senhaHex = sb.toString();
            
            pst=conexao.prepareStatement(sql);
            pst.setString(1,campoNome.getText());
            pst.setString(2,campoTelefone.getText());
            pst.setString(3,campoLogin.getText());
            pst.setString(4,senhaHex);
            pst.setString(5,comboBoxPerfil.getSelectedItem().toString());
            pst.setString(6,campoId.getText());
            if ((((campoId.getText().isEmpty())||(campoNome.getText().isEmpty())||(campoLogin.getText().isEmpty())||(campoSenha.getText().isEmpty())))) {
                JOptionPane.showMessageDialog(null, "Preencha todos os campos obrigatórios! E irmão(a) "+nomeUsuario.getText()+" digite a sua senha para confirmar as modificações!");

            } else {
                //A linha abaixo atualiza a tabela usuario com os dados do formulário.
                // A estrutura abaixo é usada para confirmar a alteração dos dados na tabela.
                int adicionado = pst.executeUpdate();
                //A linha abaixo serve de apoio.
                //System.out.println(adicionado);
                if (adicionado > 0) {
                    JOptionPane.showMessageDialog(null, "Dados do usuário alterados com sucesso!");
                    // as linhas abaixo "limpam" os campos.
                    campoId.setText(null);
                    campoNome.setText(null);
                    campoTelefone.setText(null);
                    campoLogin.setText(null);
                    campoSenha.setText(null);
                }
            }
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        
    }
    
    
    //Método responsável pela remoção de usuários.
    private void remover(){
        //A estrutura abaixo confirma a remoção do usuário.
        int confirma=JOptionPane.showConfirmDialog(null,"Tem certeza que deseja remover este usuário?","Atenção",JOptionPane.YES_NO_OPTION);
        if(confirma==JOptionPane.YES_OPTION){
            String sql="delete from tbusuarios where idusuario=?";
            try {
                pst=conexao.prepareStatement(sql);
                pst.setString(1,campoId.getText());
                int apagado = pst.executeUpdate();
                if(apagado>0){
                    JOptionPane.showMessageDialog(null,"Usuário removido com sucesso!");
                    campoId.setText(null);
                    campoNome.setText(null);
                    campoTelefone.setText(null);
                    campoLogin.setText(null);
                    campoSenha.setText(null);
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

        painelAmarelo = new javax.swing.JPanel();
        nomeId = new javax.swing.JLabel();
        nome = new javax.swing.JLabel();
        nomeLogin = new javax.swing.JLabel();
        nomeSenha = new javax.swing.JLabel();
        nomePerfil = new javax.swing.JLabel();
        campoId = new javax.swing.JTextField();
        campoNome = new javax.swing.JTextField();
        campoLogin = new javax.swing.JTextField();
        campoSenha = new javax.swing.JTextField();
        comboBoxPerfil = new javax.swing.JComboBox<>();
        nomeTelefone = new javax.swing.JLabel();
        campoTelefone = new javax.swing.JTextField();
        botaoAdicionar = new javax.swing.JButton();
        botaoDeletar = new javax.swing.JButton();
        botaoPesquisar = new javax.swing.JButton();
        botaoAtualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jDayChooser1 = new com.toedter.calendar.JDayChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Usuários");
        setPreferredSize(new java.awt.Dimension(955, 624));

        painelAmarelo.setBackground(new java.awt.Color(204, 204, 204));
        painelAmarelo.setForeground(new java.awt.Color(255, 255, 255));
        painelAmarelo.setPreferredSize(new java.awt.Dimension(955, 610));

        nomeId.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nomeId.setText("*Id:");

        nome.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nome.setText("*Nome Completo:");

        nomeLogin.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nomeLogin.setText("*Login:");

        nomeSenha.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nomeSenha.setText("*Senha:");

        nomePerfil.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nomePerfil.setText("*Perfil:");

        campoId.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoIdActionPerformed(evt);
            }
        });
        campoId.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoIdKeyPressed(evt);
            }
        });

        campoNome.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoNome.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        campoNome.setEnabled(false);
        campoNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoNomeKeyPressed(evt);
            }
        });

        campoLogin.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoLogin.setDisabledTextColor(new java.awt.Color(220, 219, 219));
        campoLogin.setEnabled(false);
        campoLogin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoLoginKeyPressed(evt);
            }
        });

        campoSenha.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoSenha.setDisabledTextColor(new java.awt.Color(220, 219, 219));
        campoSenha.setEnabled(false);
        campoSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                campoSenhaActionPerformed(evt);
            }
        });
        campoSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoSenhaKeyPressed(evt);
            }
        });

        comboBoxPerfil.setBackground(new java.awt.Color(153, 153, 255));
        comboBoxPerfil.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        comboBoxPerfil.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "admin", "moderado", "user" }));
        comboBoxPerfil.setEnabled(false);
        comboBoxPerfil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBoxPerfilActionPerformed(evt);
            }
        });

        nomeTelefone.setFont(new java.awt.Font("Century", 1, 14)); // NOI18N
        nomeTelefone.setText("Telefone:");

        campoTelefone.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        campoTelefone.setDisabledTextColor(new java.awt.Color(51, 51, 51));
        campoTelefone.setEnabled(false);
        campoTelefone.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                campoTelefoneKeyPressed(evt);
            }
        });

        botaoAdicionar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/add.png"))); // NOI18N
        botaoAdicionar.setToolTipText("Adicionar");
        botaoAdicionar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAdicionarActionPerformed(evt);
            }
        });

        botaoDeletar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/delete.png"))); // NOI18N
        botaoDeletar.setToolTipText("Deletar");
        botaoDeletar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoDeletar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoDeletarActionPerformed(evt);
            }
        });

        botaoPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/read.png"))); // NOI18N
        botaoPesquisar.setToolTipText("Pesquisar");
        botaoPesquisar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoPesquisarActionPerformed(evt);
            }
        });

        botaoAtualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/cadastroCohab/icones/update.png"))); // NOI18N
        botaoAtualizar.setToolTipText("Atualizar");
        botaoAtualizar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        botaoAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botaoAtualizarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 2, 14)); // NOI18N
        jLabel2.setText("* Campos Obrigatórios");

        jPanel1.setBackground(new java.awt.Color(92, 92, 172));

        jLabel1.setFont(new java.awt.Font("Century", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cadastro de Usuário");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(292, 292, 292))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout painelAmareloLayout = new javax.swing.GroupLayout(painelAmarelo);
        painelAmarelo.setLayout(painelAmareloLayout);
        painelAmareloLayout.setHorizontalGroup(
            painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelAmareloLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelAmareloLayout.createSequentialGroup()
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nome)
                            .addComponent(nomeLogin)
                            .addComponent(nomeSenha)
                            .addComponent(nomeId, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAmareloLayout.createSequentialGroup()
                                .addComponent(campoId, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(campoNome, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(campoSenha, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
                                .addComponent(campoLogin, javax.swing.GroupLayout.Alignment.LEADING)))
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAmareloLayout.createSequentialGroup()
                                .addGap(77, 77, 77)
                                .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nomeTelefone)
                                    .addComponent(nomePerfil))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(campoTelefone, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                                    .addComponent(comboBoxPerfil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(painelAmareloLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                .addComponent(jDayChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(36, Short.MAX_VALUE))
                    .addGroup(painelAmareloLayout.createSequentialGroup()
                        .addComponent(botaoAdicionar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(botaoPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botaoAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botaoDeletar, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        painelAmareloLayout.setVerticalGroup(
            painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(painelAmareloLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelAmareloLayout.createSequentialGroup()
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomeId)
                            .addComponent(campoId, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(34, 34, 34)
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nome)
                            .addComponent(campoNome, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(painelAmareloLayout.createSequentialGroup()
                                .addGap(58, 58, 58)
                                .addComponent(nomeLogin))
                            .addGroup(painelAmareloLayout.createSequentialGroup()
                                .addGap(51, 51, 51)
                                .addComponent(campoLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, painelAmareloLayout.createSequentialGroup()
                        .addComponent(jDayChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nomeTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(campoTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(painelAmareloLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(nomeSenha))
                    .addGroup(painelAmareloLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(campoSenha, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nomePerfil)
                            .addComponent(comboBoxPerfil, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(37, 37, 37)
                .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botaoAdicionar)
                    .addComponent(botaoDeletar)
                    .addGroup(painelAmareloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(botaoAtualizar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botaoPesquisar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelAmarelo, javax.swing.GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(painelAmarelo, javax.swing.GroupLayout.DEFAULT_SIZE, 601, Short.MAX_VALUE)
        );

        setBounds(0, 0, 955, 624);
    }// </editor-fold>//GEN-END:initComponents

    private void botaoPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoPesquisarActionPerformed
        // chamando o método consultar.
        consultar();
    }//GEN-LAST:event_botaoPesquisarActionPerformed

    private void botaoAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAdicionarActionPerformed
        // Chamando o método adicionar.
        adicionar();
    }//GEN-LAST:event_botaoAdicionarActionPerformed

    private void botaoAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoAtualizarActionPerformed
        // Chamando o método alterar.
        alterar();
    }//GEN-LAST:event_botaoAtualizarActionPerformed

    private void botaoDeletarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botaoDeletarActionPerformed
        //Chamando o método remover.
        remover();
    }//GEN-LAST:event_botaoDeletarActionPerformed

    private void comboBoxPerfilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBoxPerfilActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboBoxPerfilActionPerformed

    private void campoIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoIdActionPerformed
        
    }//GEN-LAST:event_campoIdActionPerformed

    private void campoIdKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoIdKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
            campoNome.requestFocus();
        }
    }//GEN-LAST:event_campoIdKeyPressed

    private void campoNomeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoNomeKeyPressed
         // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
           campoLogin.requestFocus();
        }
    }//GEN-LAST:event_campoNomeKeyPressed

    private void campoLoginKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoLoginKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
           campoSenha.requestFocus();
        }
    }//GEN-LAST:event_campoLoginKeyPressed

    private void campoSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoSenhaKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
           campoTelefone.requestFocus();
        }
    }//GEN-LAST:event_campoSenhaKeyPressed

    private void campoTelefoneKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_campoTelefoneKeyPressed
        // Colocando atalho enter para passar para o próximo campo
        if(evt.getKeyCode() == evt.VK_ENTER){
           comboBoxPerfil.requestFocus();
        }
    }//GEN-LAST:event_campoTelefoneKeyPressed

    private void campoSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_campoSenhaActionPerformed
      
    }//GEN-LAST:event_campoSenhaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botaoAdicionar;
    private javax.swing.JButton botaoAtualizar;
    private javax.swing.JButton botaoDeletar;
    private javax.swing.JButton botaoPesquisar;
    private javax.swing.JTextField campoId;
    public static javax.swing.JTextField campoLogin;
    public static javax.swing.JTextField campoNome;
    public static javax.swing.JTextField campoSenha;
    public static javax.swing.JTextField campoTelefone;
    public static javax.swing.JComboBox<String> comboBoxPerfil;
    private com.toedter.calendar.JDayChooser jDayChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel nome;
    private javax.swing.JLabel nomeId;
    private javax.swing.JLabel nomeLogin;
    private javax.swing.JLabel nomePerfil;
    private javax.swing.JLabel nomeSenha;
    private javax.swing.JLabel nomeTelefone;
    private javax.swing.JPanel painelAmarelo;
    // End of variables declaration//GEN-END:variables
}
