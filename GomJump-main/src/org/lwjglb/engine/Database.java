package org.lwjglb.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class Database {
    Connection conn;
    String url;

    public static class Result {
        String name;
        long id, score;

        public Result(int id, String name, int score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        public long getScore() {
            return score;
        }

        public String getName(){return name;}

        @Override
        public String toString() {
            return "id:" + id + " name:" + name + " score:" + score;
        }
    }

    public Database() {
        Path path = Paths.get("data");
        if(!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        url = "jdbc:sqlite:data/score.db";
        try {
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            String sql = "create table if not exists score(\n" +
                    "\t\"ID\"\tINTEGER,\n" +
                    "\t\"Name\"\tTEXT,\n" +
                    "\t\"Score\"\tINTEGER,\n" +
                    "\tPRIMARY KEY(\"ID\" AUTOINCREMENT)\n" +
                    ");";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertScore(String name, long score) {
        PreparedStatement stmt;
        String sql = "INSERT INTO score (name, score) VALUES(?, ?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setLong(2, score);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        var list = getScores();
        if (list.size() <= 10) return;

        long id = list.get(list.size() - 1).id;
        sql = "DELETE FROM score WHERE id = (?)";
        try {
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Result> getScores() {
        ArrayList<Result> list = new ArrayList<>();
        Statement stmt;
        ResultSet rs;
        String query = "SELECT * FROM score ORDER BY score DESC, id asc";
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            while (rs.next()) {
                list.add(new Result(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
