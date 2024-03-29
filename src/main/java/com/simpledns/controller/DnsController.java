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
    private ComboBox<String> chooseDnsCbx;
    @FXML
    private RadioButton customDnsRadioBtn;
    @FXML
    private TextField preferredDnsServerTxt;
    @FXML
    private TextField alternateDnsServerTxt;
    @FXML
    private ComboBox<String> connectNameCbx;
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
        return chooseDnsCbx.getValue();
    }

    public void getListItemForCombobox() {
        dnsList = DnsDAO.getListDnsName();
        for (int i = 0; i < dnsList.size(); i++) {
            chooseDnsCbx.getItems().add(dnsList.get(i).getDnsName());
        }
        chooseDnsCbx.setVisibleRowCount(3);
        chooseDnsCbx.getSelectionModel().selectFirst();
    }

    public void clickChooseDnsServer() {
        chooseDnsCbx.setDisable(false);
        preferredDnsServerTxt.setDisable(true);
        alternateDnsServerTxt.setDisable(true);
        getListItemForCombobox();
    }

    public void clickCustomDnsServer() {
        chooseDnsCbx.setDisable(true);
        preferredDnsServerTxt.clear();
        preferredDnsServerTxt.setDisable(false);
        alternateDnsServerTxt.clear();
        alternateDnsServerTxt.setDisable(false);
    }

    public void clickApplyDnsBtn() {
        connectionName = connectNameCbx.getValue();
        if (chooseDnsRadioBtn.isSelected()) {
            dnsList = DnsDAO.getListDnsName();
            for (Dns item : dnsList) {
                if (item.getDnsName().equalsIgnoreCase(getValueCombobox())) {
                    preferredDns = item.getPreferredDnsServer();
                    alternateDns = item.getAlternateDnsServer();
                }
            }
        } else {
            chooseDnsCbx.setValue("Custom mode");
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
        Common.runCommand("chmod +x " + fileName);
        Common.runCommand(filePath);
        Common.deleteFile(filePath);
        showSuccessAlert();
    }

    public void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Network: " + connectionName + "\nDNS Name: " + chooseDnsCbx.getValue() + "\nIP4.DNS[1]: " + preferredDns + "\nIP4.DNS[2]: " + alternateDns);
        alert.show();
    }

    public void clickRefreshBtn() {
        connectNameCbx.getItems().clear();
        String connectionName = Common.runCommandAndGetResult("nmcli -t --fields NAME con show --active");
        String[] arr = connectionName.split(System.lineSeparator());
        for (String str: arr) {
            connectNameCbx.getItems().add(str);
        }
        connectNameCbx.setVisibleRowCount(3);
        connectNameCbx.getSelectionModel().selectFirst();
    }
}