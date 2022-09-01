package com.repository.JDBC;

import com.config.JDBCConfig;
import com.context.Singleton;
import com.model.product.Manufacturer;
import com.model.product.Phone;
import com.repository.CrudRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.EnumUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Singleton
public class PhoneRepositoryDB implements CrudRepository<Phone> {
    private static final Connection CONNECTION = JDBCConfig.getConnection();

    private static PhoneRepositoryDB instance;

    public static PhoneRepositoryDB getInstance() {
        if (instance == null) {
            instance = new PhoneRepositoryDB();
        }
        return instance;
    }

    @Override
    public void save(Phone phone) {
        String sql = "INSERT INTO \"public\".\"Phone\" (id, model, manufacturer, price) VALUES (?, ?, ?, ?)";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            setObjectFields(statement, phone);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<Phone> phones) {
        String sql = "INSERT INTO \"public\".\"Phone\" (id, model, manufacturer, price) VALUES (?, ?, ?, ?)";

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            for (Phone phone : phones) {
                setObjectFields(statement, phone);
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
    private void setObjectFields(final PreparedStatement statement, final Phone phone) {
        statement.setString(1, phone.getId());
        statement.setString(2, phone.getModel());
        statement.setString(3, phone.getManufacturer().name());
        statement.setDouble(4, phone.getPrice());
    }

    @Override
    public boolean update(Phone phone) {
        String sql = "UPDATE \"public\".\"Phone\" SET model = ?, manufacturer = ?, price = ? WHERE id = ?";
        boolean hasUpdated = false;
        try (PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Phone\"")) {
            statement.setString(1, phone.getModel());
            statement.setString(2, phone.getManufacturer().name());
            statement.setDouble(3, phone.getPrice());
            statement.setString(4, phone.getId());
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("id").equals(phone.getId())
                        && resultSet.getString("model").equals(phone.getModel())
                        && resultSet.getString("manufacturer").equals(phone.getManufacturer().name())
                        && resultSet.getDouble("price") == phone.getPrice()) {
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
        String sql = "DELETE FROM  \"public\".\"Phone\" WHERE id = ?";
        boolean hasDeleted = true;
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"Phone\"")) {
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
    public List<Phone> getAll() {
        final List<Phone> result = new LinkedList<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"public\".\"Phone\"");
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @SneakyThrows
    private Phone setFieldsToObject(final ResultSet resultSet) {
        final String id = resultSet.getString("id");
        final String model = resultSet.getString("model");
        final Manufacturer manufacturer = EnumUtils.getEnum(
                Manufacturer.class,
                resultSet.getString("manufacturer"),
                Manufacturer.NONE);
        final double price = resultSet.getDouble("price");
        return new Phone(id, "", 0, price, model, manufacturer);
    }

    @Override
    public Optional<Phone> getById(String id) {
        String sql = "SELECT * FROM \"public\".\"Phone\" WHERE id = ?";
        Optional<Phone> phone = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                phone = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return phone;
    }

    @Override
    public Optional<Phone> getByIndex(int index) {
        String sql = "SELECT * FROM  \"public\".\"Phone\" LIMIT 1 OFFSET ?";
        Optional<Phone> phone = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            int rowNumber = index - 1;
            statement.setLong(1, rowNumber);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                phone = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return phone;
    }

    @Override
    public boolean hasProduct(String id) {
        String sql = "SELECT * FROM  \"public\".\"Phone\" WHERE id = ?";
        boolean phonePresent = false;

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                phonePresent = resultSet.getString("id").equals(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phonePresent;
    }

    public void clearPhone() {
        String sql = "DELETE FROM \"public\".\"Phone\"";
        try (final Statement statement = CONNECTION.createStatement()) {
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
