package com.repository;

import com.config.JDBCConfig;
import com.model.product.Manufacturer;
import com.model.product.Toaster;
import lombok.SneakyThrows;
import org.apache.commons.lang3.EnumUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ToasterRepositoryDB implements CrudRepository<Toaster> {
    private static final Connection CONNECTION = JDBCConfig.getConnection();

    private static ToasterRepositoryDB instance;

    public static ToasterRepositoryDB getInstance() {
        if (instance == null) {
            instance = new ToasterRepositoryDB();
        }
        return instance;
    }

    @Override
    public void save(Toaster toaster) {
        String sql = "INSERT INTO \"public\".\"Toaster\" (id, model, manufacturer, price) VALUES (?, ?, ?, ?)";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            setObjectFields(statement, toaster);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<Toaster> toasters) {
        String sql = "INSERT INTO \"public\".\"Toaster\" (id, model, manufacturer, price) VALUES (?, ?, ?, ?)";

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            for (Toaster toaster : toasters) {
                setObjectFields(statement, toaster);
                CONNECTION.setAutoCommit(false);
                statement.addBatch();
            }
            statement.executeBatch();
            CONNECTION.commit();
            CONNECTION.setAutoCommit(true);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private void setObjectFields(final PreparedStatement statement, final Toaster toaster) {
        statement.setString(1, toaster.getId());
        statement.setString(2, toaster.getModel());
        statement.setString(3, toaster.getManufacturer().name());
        statement.setDouble(4, toaster.getPrice());
    }

    @Override
    public boolean update(Toaster toaster) {
        String sql = "UPDATE \"public\".\"Toaster\" SET model = ?, manufacturer = ?, price = ? WHERE id = ?";
        boolean hasUpdated = false;
        try (PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Toaster\"")) {
            statement.setString(1, toaster.getModel());
            statement.setString(2, toaster.getManufacturer().name());
            statement.setDouble(3, toaster.getPrice());
            statement.setString(4, toaster.getId());
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("id").equals(toaster.getId())
                        && resultSet.getString("model").equals(toaster.getModel())
                        && resultSet.getString("manufacturer").equals(toaster.getManufacturer().name())
                        && resultSet.getDouble("price") == (toaster.getPrice())) {
                    hasUpdated = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return hasUpdated;
    }

    @Override
    public boolean delete(String id) {
        String sql = "DELETE FROM  \"public\".\"Toaster\" WHERE id = ?";
        boolean hasDeleted = true;
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Toaster\"")) {
            boolean hasBeforeDeleted = hasProduct(id);
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

    @Override
    public List<Toaster> getAll() {
        final List<Toaster> result = new LinkedList<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"public\".\"Toaster\"");
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @SneakyThrows
    private Toaster setFieldsToObject(final ResultSet resultSet) {
        final String id = resultSet.getString("id");
        final String model = resultSet.getString("model");
        final Manufacturer manufacturer = EnumUtils.getEnum(
                Manufacturer.class,
                resultSet.getString("manufacturer"),
                Manufacturer.NONE);
        final double price = resultSet.getDouble("price");
        return new Toaster.ToasterBuilder()
                .setModel(model)
                .setManufacturer(manufacturer)
                .setId(id)
                .setPrice(price)
                .build();
    }

    @Override
    public Optional<Toaster> getById(String id) {
        String sql = "SELECT * FROM  \"public\".\"Toaster\" WHERE id = ?";
        Optional<Toaster> toaster = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                toaster = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toaster;
    }

    @Override
    public Optional<Toaster> getByIndex(int index) {
        String sql = "SELECT * FROM  \"public\".\"Toaster\" LIMIT 1 OFFSET ?";
        Optional<Toaster> toaster = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            int rowNumber = index - 1;
            statement.setLong(1, rowNumber);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                toaster = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return toaster;
    }

    @Override
    public boolean hasProduct(String id) {
        String sql = "SELECT * FROM  \"public\".\"Toaster\" WHERE id = ?";
        boolean toasterPresent = false;

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                toasterPresent = resultSet.getString("id").equals(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return toasterPresent;
    }

    public void clearToaster() {
        String sql = "DELETE FROM \"public\".\"Toaster\"";
        try (final Statement statement = CONNECTION.createStatement()) {
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
