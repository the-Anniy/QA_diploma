package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.jupiter.api.Assertions;

import java.sql.DriverManager;

public class DBUtils {
    private static String url = System.getProperty("db.url");
    private static String userDB = System.getProperty("app.userDB");
    private static String password = System.getProperty("app.password");

    @SneakyThrows
    public static void clearAllData() {
        var runner = new QueryRunner();
        var conn = DriverManager.getConnection(url, userDB, password);
        runner.update(conn, "DELETE FROM credit_request_entity;");
        runner.update(conn, "DELETE FROM payment_entity;");
        runner.update(conn, "DELETE FROM order_entity;");
    }

    @SneakyThrows
    public static void checkPaymentStatus(Status status) {
        var runner = new QueryRunner();
        var conn = DriverManager.getConnection(url, userDB, password);
        var paymentDataSQL = "SELECT status FROM payment_entity;";
        var payment = runner.query(conn, paymentDataSQL, new BeanHandler<>(Payment.class));
        Assertions.assertEquals(status, payment.status);
    }

    @SneakyThrows
    public static void checkCreditStatus(Status status) {
        var runner = new QueryRunner();
        var conn = DriverManager.getConnection(url, userDB, password);
        var creditDataSQL = "SELECT status FROM credit_request_entity;";
        var credit = runner.query(conn, creditDataSQL, new BeanHandler<>(Credit.class));
        Assertions.assertEquals(status, credit.status);
    }
}