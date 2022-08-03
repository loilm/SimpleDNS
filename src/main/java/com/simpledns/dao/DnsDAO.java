package com.simpledns.dao;

import com.simpledns.model.Dns;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.Comparator;

public class DnsDAO {
    public static ObservableList<Dns> getListDnsName() {
        Dns googleDNS = new Dns("GoogleDNS", "8.8.8.8", "8.8.4.4");
        Dns cloudflareDNS = new Dns("CloudflareDNS", "1.1.1.1", "1.0.0.1");
        Dns openDNS = new Dns("OpenDNS", "208.67.222.222", "208.67.220.220");
        Dns comodoSecureDNS = new Dns("ComodoSecureDNS", "8.26.56.26", "8.20.247.20");

        ObservableList<Dns> list
                = FXCollections.observableArrayList(googleDNS, cloudflareDNS, openDNS, comodoSecureDNS);
        /*Sort list*/
        if (list.size() > 0) {
            Collections.sort(list, Comparator.comparing(Dns::getDnsName));
        }
        return list;
    }
}
