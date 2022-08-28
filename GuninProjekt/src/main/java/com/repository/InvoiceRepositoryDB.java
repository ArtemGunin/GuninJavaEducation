package com.repository;

import com.config.JDBCConfig;
import com.model.Invoice;
import com.model.product.Product;
import com.model.product.ProductType;
import lombok.SneakyThrows;
import org.apache.commons.lang3.EnumUtils;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class InvoiceRepositoryDB {

    private static final PhoneRepositoryDB PHONE_DB = PhoneRepositoryDB.getInstance();
    private static final ToasterRepositoryDB TOASTER_DB = ToasterRepositoryDB.getInstance();
    private static final TVRepositoryDB TV_DB = TVRepositoryDB.getInstance();
    private static final Connection CONNECTION = JDBCConfig.getConnection();

    public void save(Invoice invoice) {
        String sqlInvoice = "INSERT INTO \"public\".\"Invoice\" (id, sum, time, date) VALUES (?, ?, ?, ?)";
        try (final PreparedStatement invoiceStatement = CONNECTION.prepareStatement(sqlInvoice)) {
            setObjectFields(invoiceStatement, invoice);
            invoiceStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAll(List<Invoice> invoices) {
        String sqlInvoice = "INSERT INTO \"public\".\"Invoice\" (id, sum, time, date) VALUES (?, ?, ?, ?)";

        try (final PreparedStatement invoiceStatement = CONNECTION.prepareStatement(sqlInvoice)) {
            for (Invoice invoice : invoices) {
                setObjectFields(invoiceStatement, invoice);
                CONNECTION.setAutoCommit(false);
                invoiceStatement.addBatch();
            }
            invoiceStatement.executeBatch();
            CONNECTION.commit();
            CONNECTION.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void setObjectFields(final PreparedStatement invoiceStatement, final Invoice invoice) {

        invoiceStatement.setString(1, invoice.getId());
        invoiceStatement.setDouble(2, invoice.getSum());
        invoiceStatement.setTime(3, Time.valueOf(invoice.getTime().toLocalTime()));
        invoiceStatement.setDate(4, Date.valueOf(invoice.getTime().toLocalDate()));

        setInvoiceProducts(invoice);
    }

    private void setInvoiceProducts(Invoice invoice) {
        String sqlProducts = "INSERT INTO \"public\".\"Products\" (invoice, product, type) VALUES (?, ?, ?)";

        try (final PreparedStatement productsStatement = CONNECTION.prepareStatement(sqlProducts)) {
            for (Product product : invoice.getProducts()) {
                productsStatement.setString(1, invoice.getId());
                productsStatement.setString(2, product.getId());
                productsStatement.setString(3, product.getType().name());
                CONNECTION.setAutoCommit(false);
                productsStatement.addBatch();
            }
            productsStatement.executeBatch();
            CONNECTION.commit();
            CONNECTION.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Invoice> getAll() {
        final List<Invoice> result = new LinkedList<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"public\".\"Invoice\"");
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @SneakyThrows
    private Invoice setFieldsToObject(final ResultSet resultSet) {
        String invoiceId = resultSet.getString("id");
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setSum(resultSet.getDouble("sum"));
        invoice.setTime(resultSet.getTime("time").toLocalTime()
                .atDate(resultSet.getDate("date").toLocalDate()));
        invoice.setProducts(getInvoiceProducts(invoiceId));
        return invoice;
    }

    private List<Product> getInvoiceProducts(String invoiceId) {
        String sqlProducts = "SELECT * FROM \"public\".\"Products\" WHERE invoice = ?";

        final List<Product> result = new LinkedList<>();
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sqlProducts)) {
            statement.setString(1, invoiceId);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                switch (EnumUtils.getEnum(ProductType.class, resultSet.getString("type"))) {
                    case PHONE -> result.add(PHONE_DB.getById(resultSet.getString("product")).orElseThrow());
                    case TOASTER -> result.add(TOASTER_DB.getById(resultSet.getString("product")).orElseThrow());
                    case TV -> result.add(TV_DB.getById(resultSet.getString("product")).orElseThrow());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Optional<Invoice> getById(String id) {
        String sql = "SELECT * FROM \"public\".\"Invoice\" WHERE id = ?";
        Optional<Invoice> invoice = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                invoice = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return invoice;
    }

    public Optional<Invoice> getByIndex(int index) {
        String sql = "SELECT * FROM \"public\".\"Invoice\" LIMIT 1 OFFSET ?";
        Optional<Invoice> invoice = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            int rowNumber = index - 1;
            statement.setLong(1, rowNumber);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                invoice = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return invoice;
    }

    public boolean hasInvoice(String id) {
        String sql = "SELECT * FROM \"public\".\"Invoice\" WHERE id = ?";
        boolean invoicePresent = false;

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                invoicePresent = resultSet.getString("id").equals(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoicePresent;
    }

    public void clearInvoicesProducts() {
        String sql = "DELETE FROM \"public\".\"Products\"";
        try (final Statement statement = CONNECTION.createStatement()) {
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearInvoice() {
        String sql = "DELETE FROM \"public\".\"Invoice\"";
        try (final Statement statement = CONNECTION.createStatement()) {
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearAllDB() {
        PHONE_DB.clearPhone();
        TOASTER_DB.clearToaster();
        TV_DB.clearTV();
        clearInvoicesProducts();
        clearInvoice();
        System.out.println("Data base cleaned!");
    }

    public boolean update(Invoice invoice) {
        String sql = "UPDATE \"public\".\"Invoice\" SET sum = ?, time = ?, date = ? WHERE id = ?";
        boolean hasUpdated = false;
        try (PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Invoice\"")) {
            statement.setDouble(1, invoice.getSum());
            statement.setTime(2, Time.valueOf(invoice.getTime().toLocalTime()));
            statement.setDate(3, Date.valueOf(invoice.getTime().toLocalDate()));
            statement.setString(4, invoice.getId());
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("id").equals(invoice.getId())
                        && resultSet.getDouble("sum") == invoice.getSum()
                        && resultSet.getTime("time").equals(Time.valueOf(invoice.getTime().toLocalTime()))
                        && resultSet.getDate("date").equals(Date.valueOf(invoice.getTime().toLocalDate()))
                        && deleteInvoiceProducts(invoice.getId())) {
                    setInvoiceProducts(invoice);
                    hasUpdated = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hasUpdated;
    }

    public boolean updateTime(String id, LocalDateTime time) {
        String sql = "UPDATE \"public\".\"Invoice\" SET time = ?, date = ? WHERE id = ?";
        boolean hasUpdated = false;
        try (PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Invoice\" WHERE id = ?")) {
            statement.setTime(1, Time.valueOf(time.toLocalTime()));
            statement.setDate(2, Date.valueOf(time.toLocalDate()));
            statement.setString(3, id);
            statement.execute();
            statement1.setString(1, id);
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getTime("time").equals(Time.valueOf(time.toLocalTime()))
                        && resultSet.getDate("date").equals(Date.valueOf(time.toLocalDate()))) {
                    hasUpdated = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hasUpdated;
    }

    public boolean delete(String id) {
        String sql = "DELETE FROM \"public\".\"Invoice\" WHERE id = ?";
        boolean hasDeleted = true;
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Invoice\"")) {
            boolean hasBeforeDeleted = hasInvoice(id);
            deleteInvoiceProducts(id);
            if (!hasBeforeDeleted) {
                return false;
            }
            statement.setString(1, id);
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("id").equals(id)) {
                    hasDeleted = false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hasDeleted;
    }

    public boolean deleteInvoiceProducts(String id) {
        String sql = "DELETE FROM \"public\".\"Products\" WHERE invoice = ?";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Products\"")) {
            statement.setString(1, id);
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("invoice").equals(id)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean deleteInvoiceProduct(String invoiceId, String productId) {
        String sqlCheck = "SELECT * FROM \"public\".\"Products\"";
        String sqlProduct = "DELETE FROM \"public\".\"Products\" WHERE invoice = ? AND product = ?";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sqlProduct);
             final PreparedStatement statement1 = CONNECTION.prepareStatement(sqlCheck)) {
            statement.setString(1, invoiceId);
            statement.setString(2, productId);
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("invoice").equals(invoiceId)
                        && resultSet.getString("product").equals(productId)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean addInvoiceProduct(String invoiceId, Product product) {
        String sql = "INSERT INTO \"public\".\"Products\" (invoice, product, type) VALUES (?, ?, ?)";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql);
             final Statement statement1 = CONNECTION.createStatement()) {
            statement.setString(1, invoiceId);
            statement.setString(2, product.getId());
            statement.setString(3, product.getType().name());
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery("SELECT * FROM \"public\".\"Products\"");
            while (resultSet.next()) {
                if (resultSet.getString("invoice").equals(invoiceId)
                        && resultSet.getString("product").equals(product.getId())) {
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Invoice> getInvoiceListWithSumConditions(String sql, double sumCondition) {
        final List<Invoice> result = new LinkedList<>();
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setDouble(1, sumCondition);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public int getCountBy(String sql, String parameter) {
        int result = 0;
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result = resultSet.getInt(parameter);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Map<Double, Integer> groupInvoicesBy(String sql, String numericColumn, String function) {
        Map<Double, Integer> groups = new HashMap<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                groups.put(resultSet.getDouble(numericColumn), resultSet.getInt(function));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }
}
