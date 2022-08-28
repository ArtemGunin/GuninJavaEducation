package com;

import com.repository.InvoiceRepositoryDB;
import com.verification.JDBC_Operations;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        new InvoiceRepositoryDB().clearAllDB();

        new JDBC_Operations().run();
    }
}
