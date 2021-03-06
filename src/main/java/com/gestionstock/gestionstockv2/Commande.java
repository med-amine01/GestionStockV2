package com.gestionstock.gestionstockv2;

import Classes.Clnt;
import Classes.Cmd;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class Commande implements Initializable {


    //-------------- DONNÉES COMMANDE -----------------
    @FXML
    private TableView<Cmd> tableCMD;
    @FXML
    private TableColumn<Cmd, Integer> IDCMDcol;
    @FXML
    private TableColumn<Cmd, Double> MONTcol;
    @FXML
    private TableColumn<Cmd, String> CLIENTcol;
    @FXML
    private TableColumn<Cmd, String> DATEcol;
    @FXML
    private TableColumn<Cmd, Integer> EMPcol;
    @FXML
    private TableColumn<Cmd, String> ETATcol;



    //------------------ DONNÉES CLIENT ----------------
    @FXML
    private TableView<Clnt> tableClient;
    @FXML
    private TableColumn<Clnt, String> IDCLIENTcol;
    @FXML
    private TableColumn<Clnt, String> ADDcol;
    @FXML
    private TableColumn<Clnt, String> NOMcol;
    @FXML
    private TableColumn<Clnt, String> PRENOMcol;
    @FXML
    private TableColumn<Clnt, String> Telcol;
    @FXML
    private TableColumn<Clnt, String> mailCol;



    //-----------------------------------
    @FXML
    private CheckBox cbCMD;
    @FXML
    private TextField client;
    @FXML
    private TextField date;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField inputCMD;
    @FXML
    private TextField inputClient;
    @FXML
    private Label mntTot;
    @FXML
    private Label user;

    private Stage stage;
    private Scene scene;
    private Parent root;

    Connection con;
    PreparedStatement pst,pst1,pst2,pst3;
    ObservableList<Clnt> listClient;
    ObservableList<Cmd> listCmd;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

    private static String idemp;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        datePicker.setPromptText(LocalDate.now().format(dateFormatter));
        date.setText(LocalDate.now().format(dateFormatter));
        datePicker.setConverter(new StringConverter<>() {

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        listClient = getClients("");
        ActualiserClient(listClient);

        listCmd = getCommandes("");
        ActualiserCommande(listCmd);
    }

    //-------------------- Connection à la base de donnée -----------------------
    public void connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/gestionstock", "root","");

        }
        catch (ClassNotFoundException ex)
        {
            ex.printStackTrace();

        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
        }
    }

    //---------------------- chargement du tableau dans une liste-----------------------------
    public ObservableList<Clnt> getClients(String sqlSearch)
    {
        ObservableList<Clnt> clientList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {
            if(sqlSearch.equals(""))
            {
                pst = con.prepareStatement("select * from client where etat = 0");
                rs= pst.executeQuery();
            }
            else
            {
                pst = con.prepareStatement(sqlSearch);
                rs = pst.executeQuery();
            }

            Clnt clients;

            while (rs.next())
            {
                clients = new Clnt(rs.getString("cinClient"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("tel"),
                        rs.getString("mail"),
                        rs.getString("adresse"));
                clientList.add(clients);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return clientList;
    }

    //---------------------- chargement du tableau dans une liste-----------------------------
    public ObservableList<Cmd> getCommandes(String sqlSearch)
    {
        ObservableList<Cmd> cmdList = FXCollections.observableArrayList();
        connect();
        ResultSet rs;
        try
        {
            if(sqlSearch.equals(""))
            {
                pst = con.prepareStatement("select * from commande where etat = 0");
                rs= pst.executeQuery();
            }
            else
            {
                pst = con.prepareStatement(sqlSearch);
                rs = pst.executeQuery();
            }

            Cmd cmds;

            while (rs.next())
            {
                cmds = new Cmd(rs.getInt("idcmd"),
                        rs.getInt("idemp"),
                        rs.getString("cinClient"),
                        rs.getString("datecmd"),
                        rs.getDouble("montantTot"),
                        rs.getString("etatcmd"));
                cmdList.add(cmds);
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return cmdList;
    }
    public void ActualiserClient(ObservableList<Clnt> list)
    {
        IDCLIENTcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("cinClient"));
        NOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("nom"));
        PRENOMcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("prenom"));
        Telcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("tel"));
        mailCol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("mail"));
        ADDcol.setCellValueFactory(new PropertyValueFactory<Clnt,String>("adresse"));

        tableClient.setItems(list);
    }
    public void ActualiserCommande(ObservableList<Cmd> list)
    {
        IDCMDcol.setCellValueFactory(new PropertyValueFactory<Cmd,Integer>("idcmd"));
        CLIENTcol.setCellValueFactory(new PropertyValueFactory<Cmd,String>("cinClient"));
        EMPcol.setCellValueFactory(new PropertyValueFactory<Cmd,Integer>("idemp"));
        DATEcol.setCellValueFactory(new PropertyValueFactory<Cmd,String>("datecmd"));
        MONTcol.setCellValueFactory(new PropertyValueFactory<Cmd,Double>("montant"));
        ETATcol.setCellValueFactory(new PropertyValueFactory<Cmd,String>("etatcmd"));

        tableCMD.setItems(list);
    }


    //------------------------------------- CRUD CMD -----------------------------------------------------
    @FXML
    void AjouterCmdClick(ActionEvent event)
    {
        String nomclient = client.getText();
        String idcl = "";
        int idcmd = 0;

        if(nomclient.equals(""))
        {
            Message("Verifiez vos champs");
        }
        else
        {
            try
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Ajouter une commande");
                alert.setContentText("Vous allez créer une commande \nVoulez-vous poursuivre ?\n CLIENT : <"+nomclient+">");

                Optional<ButtonType> r = alert.showAndWait();
                if (r.get() == ButtonType.OK){
                    ResultSet rs, rs2;
                    pst = con.prepareStatement("select cinClient from client where nom = '"+nomclient+"'");
                    rs = pst.executeQuery();

                    while (rs.next())
                    {
                        idcl = rs.getString("cinClient");
                    }

                    LocalDate d = LocalDate.now();
                    String currentDate = d.format(dateFormatter).concat("-").concat(String.valueOf(LocalTime.now()));
                    pst2 = con.prepareStatement("insert into commande (idemp,cinClient,datecmd,montantTot,etatcmd,etat) values (?,?,?,?,?,?)");
                    pst2.setString(1, getIdemp());
                    pst2.setString(2, idcl);
                    pst2.setString(3, currentDate);
                    pst2.setString(4, "0");
                    pst2.setString(5, "EnCours");
                    pst2.setString(6, "0");
                    pst2.executeUpdate();
                    Message("La commande a été créee");
                    listCmd = getCommandes("");
                    ActualiserCommande(listCmd);


                    pst3 = con.prepareStatement("select idcmd from commande where idemp = '"+getIdemp()+"' and cinClient = '"+idcl+"' and etatcmd = 'EnCours'");
                    rs2 = pst.executeQuery();
                    while (rs2.next())
                    {
                        idcmd = rs2.getInt("idcmd");
                    }
                    LigneCmdWindow(idcmd,event);
                    viderClick(event);
                    printSumPrixCmd();

                }

            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    @FXML
    void rechClientClick(ActionEvent event)
    {
        String rech = inputClient.getText().trim();
        if(rech.equals(""))
        {
            listClient = getClients("");
            ActualiserClient(listClient);
        }
        else
        {
            if(ChampsIdEstInt(rech))
            {
                String rqt = "select cinClient,nom,prenom,tel,mail,adresse from client where etat = 0 and cinClient = "+rech;
                listClient = getClients(rqt);
                if(listClient.isEmpty())
                {
                    Message("CIN <"+rech+"> n'existe pas !!");
                    inputClient.setText("");
                    inputClient.requestFocus();
                }
                else
                {
                    ActualiserClient(listClient);
                }
            }
            else
            {
                String rqt ="select cinClient,nom,prenom,tel,mail,adresse from client where (nom like '"+rech+"%' or prenom like '"+rech+"%' or mail like '"+rech+"%' or adresse like '"+rech+"%') and etat=0";
                listClient = getClients(rqt);
                if(listClient.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !");
                    inputClient.setText("");
                    inputClient.requestFocus();
                }
                else
                {
                    ActualiserClient(listClient);
                }
            }
        }
    }
    @FXML
    void rechCmdClick(ActionEvent event)
    {
        String rech = inputCMD.getText().trim();
        String date = dateClick(event);

        if(rech.equals(""))
        {
            if(date.equals(""))
            {
                if(cbCMD.isSelected())
                {
                    String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp and commande.etatcmd ='Passer'" +
                            "HAVING datecmd like '"+LocalDate.now().format(dateFormatter)+"%' ";
                    listCmd = getCommandes(rqt);
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
                else
                {
                    String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp " +
                            "HAVING datecmd like '"+LocalDate.now().format(dateFormatter)+"%' ";
                    listCmd = getCommandes(rqt);
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
            }
            else
            {
                String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp and commande.etat = 0 " +
                        "HAVING datecmd like '"+date+"%'";
                listCmd = getCommandes(rqt);
                ActualiserCommande(listCmd);
                datePicker.setValue(null);
            }

        }
        else
        {
            if(ChampsIdEstInt(rech)) //3RAFNA ELI HOA YFARKESS BEL ID
            {
                String rqt = "select commande.* , employe.nom from commande,employe where commande.idemp = employe.idemp " +
                        "HAVING datecmd like '"+date+"%' and ( cinClient like '"+rech+"%' or idcmd like '"+rech+"%')";
                listCmd = getCommandes(rqt);

                if(listCmd.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !!");
                    inputCMD.setText("");
                    inputCMD.requestFocus();
                }
                else
                {
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
            }
            else
            {
                String rqt = "select commande.* , client.nom, employe.nom from commande, client, employe " +
                        "where commande.cinClient = client.cinClient and date like '"+date+"%' HAVING client.nom like = '"+rech+"%'";
                listCmd = getCommandes(rqt);
                if(listCmd.isEmpty())
                {
                    Message("<"+rech+"> n'existe pas !!");
                    inputCMD.setText("");
                    inputCMD.requestFocus();
                }
                else
                {
                    ActualiserCommande(listCmd);
                    datePicker.setValue(null);
                }
            }
        }
    }

    @FXML
    void ImprimerCmdClick(ActionEvent event) throws FileNotFoundException
    {
        Cmd cmd = tableCMD.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Imprimer La Facture !");
        alert.setContentText("Vous allez Imprimer La facture de cette commande ? \nVoulez-vous poursuivre ?\n Commande : <"+cmd.getIdcmd()+">");

        Optional<ButtonType> r = alert.showAndWait();
        if (r.get() == ButtonType.OK)
        {

            String path = "/home/madchicken/facture/"+ cmd.getCinClient() +"__"+ String.valueOf(cmd.getIdcmd()).concat(".pdf");

            PdfWriter pdfWriter = new PdfWriter(path);

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();
            Document document = new Document(pdfDocument);
            pdfDocument.setDefaultPageSize(PageSize.A4);


            float [] columnWidthHeader  = {520f};
            Table headerTable = new Table(columnWidthHeader);
            headerTable.setBackgroundColor(new DeviceRgb(0 ,5,68)).setFontColor(Color.WHITE);

            headerTable.addCell(new Cell().add("GESTION DE STOCK"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMarginTop(30f)
                    .setMarginBottom(30f)
                    .setFontSize(30f)
                    .setBorder(Border.NO_BORDER);


            float [] columnWidthInformation  = {260f,260f};
            Table InformationTable = new Table(columnWidthInformation);

            try
            {
                ResultSet rs1 ;
                pst = con.prepareStatement("select employe.nom , commande.datecmd, commande.idcmd, commande.cinClient, client.nom, client.tel from employe, commande, client " +
                        "where commande.idemp = employe.idemp and commande.cinClient = client.cinClient and idcmd = "+cmd.getIdcmd());
                rs1 = pst.executeQuery();

                while (rs1.next())
                {
                    InformationTable.addCell(new Cell().add("VENDEUR : "+rs1.getString("nom")).setBorder(Border.NO_BORDER));
                    InformationTable.addCell(new Cell().add("CIN : " +rs1.getString("cinClient")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                    InformationTable.addCell(new Cell().add("DATE : "+rs1.getString("datecmd")).setBorder(Border.NO_BORDER));
                    InformationTable.addCell(new Cell().add("CLIENT : "+rs1.getString("nom")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                    InformationTable.addCell(new Cell().add("REF COMMANDE : "+rs1.getString("idcmd")).setBorder(Border.NO_BORDER));
                    InformationTable.addCell(new Cell().add("TÉLEPHONE : "+rs1.getString("tel")).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER));
                }

            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }



            float [] columnWidth  = {200f, 100f, 100f, 120f};
            Table table = new Table(columnWidth);
            table.addCell(new Cell().add("Nom Pièce").setBackgroundColor(new DeviceRgb(0 ,5,68)).setFontColor(Color.WHITE));
            table.addCell(new Cell().add("Quantité").setBackgroundColor(new DeviceRgb(0 ,5,68)).setFontColor(Color.WHITE));
            table.addCell(new Cell().add("Prix Unitaire").setBackgroundColor(new DeviceRgb(0 ,5,68)).setFontColor(Color.WHITE));
            table.addCell(new Cell().add("Montant").setBackgroundColor(new DeviceRgb(0 ,5,68)).setFontColor(Color.WHITE));

            float [] columnWidthMnt = {100f};
            Table tableMnt = new Table(columnWidthMnt);


            tableMnt.addCell(new Cell().add("Montant Total").setBackgroundColor(new DeviceRgb(0 ,5,68)).setFontColor(Color.WHITE))
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT)
                    .setVerticalAlignment(VerticalAlignment.BOTTOM);

            try
            {
                ResultSet rs ;
                pst = con.prepareStatement("select piece.marque, lignecmd.qtelc, piece.prixunitaire, lignecmd.prixlc from piece, lignecmd " +
                        "where lignecmd.idpiece = piece.idpiece and lignecmd.idcmd =  "+cmd.getIdcmd());
                rs = pst.executeQuery();

                while (rs.next())
                {
                    table.addCell(new Cell().add(rs.getString("marque")));
                    table.addCell(new Cell().add(rs.getString("qtelc")));
                    table.addCell(new Cell().add(rs.getString("prixunitaire")));
                    table.addCell(new Cell().add(rs.getString("prixlc")));
                }

                tableMnt.addCell(new Cell().add(String.valueOf(cmd.getMontant())));
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }

            document.add(headerTable);
            document.add(new Paragraph("\n\n"));
            document.add(InformationTable);
            document.add(new Paragraph("\n\n"));
            document.add(table);
            document.add(new Paragraph("\n\n"));
            document.add(tableMnt);

            document.add(new Paragraph("MERCI À BIENTÔT")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMarginTop(280f));


            document.close();
        }


    }
    //--------------------------------------------------------------------------------------------------

    //--------------------------------- LIGNE CMD WINDOW ---------------------------------
    public void LigneCmdWindow(int idcmd, ActionEvent event) throws IOException
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("lignecmd.fxml"));
            root = loader.load();
            Lignecmd lignecmd = loader.getController();
            lignecmd.setCmd(idcmd);
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void backClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("intervendeur.fxml"));
            root = loader.load();
            InterVendeur interVendeur = loader.getController();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            interVendeur.countNb();
            interVendeur.PrintUserName(user.getText());
            stage.close();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    String dateClick(ActionEvent event)
    {
        try {
            return datePicker.getValue().format(dateFormatter);
        }
        catch (Exception e)
        {
            return "";
        }
    }
    @FXML
    void decClick(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            root = loader.load();
            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void ligneClientClick(MouseEvent event)
    {
        try {
            Clnt clnt = tableClient.getSelectionModel().getSelectedItem();
            client.setText(clnt.getNom());
        }catch (NullPointerException e)
        {
            Message("Aucune ligne n'est sélectionnée");
        }
    }
    @FXML
    void lignecmdClick(MouseEvent event)
    {
        try {
            ResultSet rs ;
            Cmd cmd = tableCMD.getSelectionModel().getSelectedItem();
            pst1 = con.prepareStatement("select nom from client where cinClient = "+ cmd.getCinClient());
            rs = pst1.executeQuery();
            while (rs.next())
            {
                client.setText(rs.getString("nom"));
            }

        }catch (NullPointerException e)
        {
            Message("Aucune ligne n'est sélectionnée");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }

    }



    @FXML
    void viderClick(ActionEvent event) {
        client.setText("");
    }


    //--------------------- set user name (passage entre les controlleurs) -------------------
    public void PrintUserName(String CurrentUserName)
    {
        user.setText(CurrentUserName);
    }
    //------------------------- alert message ---------------------
    private void Message(String msg)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }


    public static String getIdemp() {
        return idemp;
    }

    public void setIdemp(String idemp) {
        this.idemp = idemp;
    }

    //----------------------------id chiffres-----------------------------
    public boolean ChampsIdEstInt(String champsId)
    {
        boolean b  ;
        try
        {
            Integer.parseInt(champsId);
            b = true;
        }
        catch(NumberFormatException e)
        {
            b = false;
        }
        return b;
    }

    public void printSumPrixCmd()
    {
        try
        {
            //select idemp,sum(montantTot) from commande GROUP By idemp;
            double mnt = 0.0;
            ResultSet rs ;
            pst = con.prepareStatement("select sum(montantTot) from commande where datecmd like '"+LocalDate.now().format(dateFormatter)+"%' and idemp = "+getIdemp());
            rs = pst.executeQuery();
            while (rs.next())
            {
                mnt = rs.getDouble("sum(montantTot)");
            }

            mntTot.setText(Double.parseDouble(new DecimalFormat("#####.####").format(mnt).replace(',' , '.'))+" DT");

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
