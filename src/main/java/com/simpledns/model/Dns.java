package com.simpledns.model;

public class Dns {
    private String dnsName;
    private String preferredDnsServer;
    private String alternateDnsServer;

    public Dns(String dnsName, String preferredDnsServer, String alternateDnsServer) {
        this.dnsName = dnsName;
        this.preferredDnsServer = preferredDnsServer;
        this.alternateDnsServer = alternateDnsServer;
    }

    public String getDnsName() {
        return dnsName;
    }

    public void setDnsName(String dnsName) {
        this.dnsName = dnsName;
    }

    public String getPreferredDnsServer() {
        return preferredDnsServer;
    }

    public void setPreferredDnsServer(String preferredDnsServer) {
        this.preferredDnsServer = preferredDnsServer;
    }

    public String getAlternateDnsServer() {
        return alternateDnsServer;
    }

    public void setAlternateDnsServer(String alternateDnsServer) {
        this.alternateDnsServer = alternateDnsServer;
    }
}
