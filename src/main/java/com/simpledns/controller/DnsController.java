package com.simpledns.controller;

import com.simpledns.common.Common;
import com.simpledns.dao.DnsDAO;
import com.simpledns.model.Dns;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class DnsController {
    @FXML
    private ToggleGroup chooseDnsGroup;
    @FXML
    private RadioButton chooseDnsRadioBtn;
    @FXML
    private ComboBox<String> chooseDnsCombobox;
    @FXML
    private RadioButton customDnsRadioBtn;
    @FXML
    private TextField preferredDnsServerTxt;
    @FXML
    private TextField alternateDnsServerTxt;
    @FXML
    private TextField connectionNameTxt;
    private ObservableList<Dns> dnsList;
    private String connectionName;
    private String preferredDns = "", alternateDns = "";
    private String currentPath = System.getProperty("user.dir");

    public void init() {
        chooseDnsGroup = new ToggleGroup();
        chooseDnsRadioBtn.setToggleGroup(chooseDnsGroup);
        customDnsRadioBtn.setToggleGroup(chooseDnsGroup);
        chooseDnsRadioBtn.setSelected(true);
        clickRefreshBtn();
        if (chooseDnsRadioBtn.isSelected()) {
            clickChooseDnsServer();
            getListItemForCombobox();
        }
    }

    public String getValueCombobox() {
        return chooseDnsCombobox.getValue();
    }

    public void getListItemForCombobox() {
        dnsList = DnsDAO.getListDnsName();
        for (int i = 0; i < dnsList.size(); i++) {
            chooseDnsCombobox.getItems().add(dnsList.get(i).getDnsName());
        }
        chooseDnsCombobox.setVisibleRowCount(3);
        chooseDnsCombobox.getSelectionModel().selectFirst();
    }

    public void clickChooseDnsServer() {
        chooseDnsCombobox.setDisable(false);
        preferredDnsServerTxt.setDisable(true);
        alternateDnsServerTxt.setDisable(true);
    }

    public void clickCustomDnsServer() {
        chooseDnsCombobox.setDisable(true);
        preferredDnsServerTxt.clear();
        preferredDnsServerTxt.setDisable(false);
        alternateDnsServerTxt.clear();
        alternateDnsServerTxt.setDisable(false);
    }

    public void clickApplyDnsBtn() {
        connectionName = connectionNameTxt.getText();
        if (chooseDnsRadioBtn.isSelected()) {
            dnsList = DnsDAO.getListDnsName();
            for (Dns item : dnsList) {
                if (item.getDnsName().equalsIgnoreCase(getValueCombobox())) {
                    preferredDns = item.getPreferredDnsServer();
                    alternateDns = item.getAlternateDnsServer();
                }
            }
        } else {
            preferredDns = preferredDnsServerTxt.getText();
            alternateDns = alternateDnsServerTxt.getText();
        }

        String contentChangeDns = "nmcli con mod \'" + connectionName + "\' ipv4.dns \'" + preferredDns + " " + alternateDns + "\'";
        String contentRestartNetwork = "nmcli con down \'" + connectionName + "\' && nmcli con up \'" + connectionName + "\'";
        String fileName = "temp.sh";
        String filePath = currentPath + "/" + fileName;
        Common.createFile(filePath);
        Common.writeToFile(filePath, contentChangeDns);
        Common.writeToFile(filePath, contentRestartNetwork);
        Common.runCommand(filePath);
        Common.deleteFile(filePath);
        showSuccessAlert();
    }

    public void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Network: " + connectionName + "\nDNS Name: " + chooseDnsCombobox.getValue() + "\nIP4.DNS[1]: " + preferredDns + "\nIP4.DNS[2]: " + alternateDns);
        alert.show();
    }

    public void clickRefreshBtn() {
        String connectionName = Common.runCommandAndGetResult("iwgetid -r");
        connectionNameTxt.setText(connectionName);
    }
}