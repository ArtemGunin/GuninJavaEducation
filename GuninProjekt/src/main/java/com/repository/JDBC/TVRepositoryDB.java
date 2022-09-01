package com.repository.JDBC;

import com.config.JDBCConfig;
import com.context.Singleton;
import com.model.product.Manufacturer;
import com.model.product.TV;
import com.repository.CrudRepository;
import lombok.SneakyThrows;
import org.apache.commons.lang3.EnumUtils;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Singleton
public class TVRepositoryDB implements CrudRepository<TV> {
    private static final Connection CONNECTION = JDBCConfig.getConnection();

    private static TVRepositoryDB instance;

    public static TVRepositoryDB getInstance() {
        if (instance == null) {
            instance = new TVRepositoryDB();
        }
        return instance;
    }

    @Override
    public void save(TV tv) {
        String sql = "INSERT INTO \"public\".\"TV\" (id, model, manufacturer, price) VALUES (?, ?, ?, ?)";
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            setObjectFields(statement, tv);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll(List<TV> tvs) {
        String sql = "INSERT INTO \"public\".\"TV\" (id, model, manufacturer, price) VALUES (?, ?, ?, ?)";

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            for (TV tv : tvs) {
                setObjectFields(statement, tv);
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
    private void setObjectFields(final PreparedStatement statement, final TV tv) {
        statement.setString(1, tv.getId());
        statement.setString(2, tv.getModel());
        statement.setString(3, tv.getManufacturer().name());
        statement.setDouble(4, tv.getPrice());
    }

    @Override
    public boolean update(TV tv) {
        String sql = "UPDATE \"public\".\"TV\" SET model = ?, manufacturer = ?, price = ? WHERE id = ?";
        boolean hasUpdated = false;
        try (PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"TV\"")) {
            statement.setString(1, tv.getModel());
            statement.setString(2, tv.getManufacturer().name());
            statement.setDouble(3, tv.getPrice());
            statement.setString(4, tv.getId());
            statement.execute();
            final ResultSet resultSet = statement1.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("id").equals(tv.getId())
                        && resultSet.getString("model").equals(tv.getModel())
                        && resultSet.getString("manufacturer").equals(tv.getManufacturer().name())
                        && resultSet.getDouble("price") == (tv.getPrice())) {
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
        String sql = "DELETE FROM  \"public\".\"TV\" WHERE id = ?";
        boolean hasDeleted = true;
        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql);
             PreparedStatement statement1 = CONNECTION.prepareStatement("SELECT * FROM \"public\".\"TV\"")) {
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
    public List<TV> getAll() {
        final List<TV> result = new LinkedList<>();
        try (final Statement statement = CONNECTION.createStatement()) {
            final ResultSet resultSet = statement.executeQuery("SELECT * FROM \"public\".\"TV\"");
            while (resultSet.next()) {
                result.add(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @SneakyThrows
    private TV setFieldsToObject(final ResultSet resultSet) {
        final String id = resultSet.getString("id");
        final String model = resultSet.getString("model");
        final Manufacturer manufacturer = EnumUtils.getEnum(
                Manufacturer.class,
                resultSet.getString("manufacturer"),
                Manufacturer.NONE);
        double price = resultSet.getDouble("price");
        return new TV(id, "", 0, price, model, manufacturer, 0);
    }

    @Override
    public Optional<TV> getById(String id) {
        String sql = "SELECT * FROM  \"public\".\"TV\" WHERE id = ?";
        Optional<TV> tv = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tv = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tv;
    }

    @Override
    public Optional<TV> getByIndex(int index) {
        String sql = "SELECT * FROM  \"public\".\"TV\" LIMIT 1 OFFSET ?";
        Optional<TV> tv = Optional.empty();

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            int rowNumber = index - 1;
            statement.setLong(1, rowNumber);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tv = Optional.of(setFieldsToObject(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tv;
    }

    @Override
    public boolean hasProduct(String id) {
        String sql = "SELECT * FROM  \"public\".\"TV\" WHERE id = ?";
        boolean tvPresent = false;

        try (final PreparedStatement statement = CONNECTION.prepareStatement(sql)) {
            statement.setString(1, id);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                tvPresent = resultSet.getString("id").equals(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tvPresent;
    }

    public void clearTV() {
        String sql = "DELETE FROM \"public\".\"TV\"";
        try (final Statement statement = CONNECTION.createStatement()) {
            statement.execute(sql);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
