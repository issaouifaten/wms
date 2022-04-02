package com.darryncampbell.dwgettingstartedjava.Model.Return;

public class SelectReturn {
    String NoDoc;
    String NoClient;

    public String getNoDoc() {
        return NoDoc;
    }

    public void setNoDoc(String noDoc) {
        NoDoc = noDoc;
    }

    public String getNoClient() {
        return NoClient;
    }

    public void setNoClient(String noClient) {
        NoClient = noClient;
    }

    public SelectReturn(String noDoc, String noClient) {
        NoDoc = noDoc;
        NoClient = noClient;
    }
}
