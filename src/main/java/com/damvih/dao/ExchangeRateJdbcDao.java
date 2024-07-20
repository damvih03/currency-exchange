package com.damvih.dao;

import com.damvih.entities.Currency;
import com.damvih.entities.ExchangeRate;
import com.damvih.exceptions.AlreadyExistsException;
import com.damvih.exceptions.DatabaseOperationException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateJdbcDao extends JdbcDao<ExchangeRate> {

    @Override
    public ExchangeRate create(ExchangeRate entity) {
        String sqlQuery = "INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setLong(1, entity.getBaseCurrency().getId());
            preparedStatement.setLong(2, entity.getTargetCurrency().getId());
            preparedStatement.setBigDecimal(3, entity.getRate());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            Long id = resultSet.getLong(1);
            entity.setId(id);
            return entity;
        } catch (SQLException exception) {
            if (isUniqueError(exception)) {
                throw new AlreadyExistsException("Exchange rate already exists");
            }
            throw new DatabaseOperationException();
        }
    }

    @Override
    public List<ExchangeRate> findAll() {
        String sqlQuery = """
                SELECT
                    exchange_rates.id AS id,
                    base_currency.id AS base_id,
                    base_currency.code AS base_code,
                    base_currency.name AS base_name,
                    base_currency.sign AS base_sign,
                    target_currency.id AS target_id,
                    target_currency.code AS target_code,
                    target_currency.name AS target_name,
                    target_currency.sign AS target_sign,
                    exchange_rates.rate AS rate
                FROM exchange_rates
                JOIN currencies base_currency ON base_currency_id = base_currency.id
                JOIN currencies target_currency ON target_currency_id = target_currency.id
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<ExchangeRate> exchangeRates = new ArrayList<>();
            while (resultSet.next()) {
                exchangeRates.add(getExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException exception) {
            throw new DatabaseOperationException();
        }
    }

    public ExchangeRate update(ExchangeRate entity) {
        String sqlQuery = "UPDATE exchange_rates SET rate = ? WHERE id = ?";
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setBigDecimal(1, entity.getRate());
            preparedStatement.setLong(2, entity.getId());

            preparedStatement.executeUpdate();
            return entity;
        } catch (SQLException exception) {
            throw new DatabaseOperationException();
        }
    }

    public Optional<ExchangeRate> findByCodes(String baseCode, String targetCode) {
        String sqlQuery = """
                SELECT
                    exchange_rates.id AS id,
                    base_currency.id AS base_id,
                    base_currency.code AS base_code,
                    base_currency.name AS base_name,
                    base_currency.sign AS base_sign,
                    target_currency.id AS target_id,
                    target_currency.code AS target_code,
                    target_currency.name AS target_name,
                    target_currency.sign AS target_sign,
                    exchange_rates.rate AS rate
                FROM exchange_rates
                JOIN currencies base_currency ON base_currency_id = base_currency.id
                JOIN currencies target_currency ON target_currency_id = target_currency.id
                WHERE base_code = ? AND target_code = ?
                """;
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, baseCode);
            preparedStatement.setString(2, targetCode);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(getExchangeRate(resultSet));
            }
            return Optional.empty();
        } catch (SQLException exception) {
            throw new DatabaseOperationException();
        }
    }

    private ExchangeRate getExchangeRate(ResultSet resultSet) throws SQLException {
        return new ExchangeRate(
                resultSet.getLong("id"),
                new Currency(
                        resultSet.getLong("base_id"),
                        resultSet.getString("base_code"),
                        resultSet.getString("base_name"),
                        resultSet.getString("base_sign")
                ),
                new Currency(
                        resultSet.getLong("target_id"),
                        resultSet.getString("target_code"),
                        resultSet.getString("target_name"),
                        resultSet.getString("target_sign")
                ),
                resultSet.getBigDecimal("rate")
        );
    }

}
