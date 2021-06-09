/*Conexão com o banco de dados.*/
package br.com.cadastroCohab.dal;

import java.sql.*;

/**
 *
 * @author Fabiana de Albuquerque 
 */
public class ModuloConexao {
    //Método responsável por estabelecer a conexão com o banco.
    public static Connection conector(){
        java.sql.Connection conexao = null;
        //A linha abaixo chama o driver.
        String driver = "com.mysql.jdbc.Driver";
        //Armazenando informações referente ao banco.
        String url="jdbc:mysql://db4free.net:3306/dbcadastroscapec";
        String user ="scapec";
        String password = "4vHyG8&PVND0c-p>";
        //Estabelecendo a conexão com o banco.
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, password);
            return conexao;
        } catch (Exception e) {
            // A linha abaixo serve de apoio para esclarecer o erro.
           // System.out.println(e);
            return null;
        }
    }

    public static void executeSQL(String select__from_tbmembros_where_classe) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
