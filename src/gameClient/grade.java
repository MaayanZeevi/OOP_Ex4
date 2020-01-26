package gameClient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class grade {
    /*
     * This method returns how much this id player played the game
     *
     */
    public static int frequenceOfGame(int id){
        //count how much time this id have played this curr stage
        int countTimeOfGames = 0;
        try {
            String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
            String jdbcUser="student";
            String jdbcUserPassword="OOP2020student";
            String allCustomersQuery = "SELECT * FROM Logs where userID="+ id +";";
            ResultSet resultSet = null;
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(allCustomersQuery);
            while (resultSet.next()) {
                countTimeOfGames++;
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return countTimeOfGames;
    }

    /*
    /This method calculate how much players have higher score from the curr player in the curr stage
    */
    public static int global(int id, int level, int yourScore) {
        int higherGrades = 1;
        int moves = 0, Grade=0;
        switch(level) {
            case 0:
                moves = 290;
                Grade = 125;
                break;

            case 1:
                moves = 580;
                Grade = 436;
                break;

            case 3:
                moves = 580;
                Grade = 713;
                break;

            case 5:
                moves = 500;
                Grade = 570;
                break;

            case 9:
                moves = 580;
                Grade = 480;
                break;

            case 11:
                moves = 580;
                Grade = 1050;
                break;

            case 13:
                moves = 580;
                Grade = 310;
                break;

            case 16:
                moves = 290;
                Grade = 235;
                break;

            case 19:
                moves = 580;
                Grade = 250;
                break;

            case 20:
                moves = 290;
                Grade = 200;
                break;

            case 23:
                moves = 1140;
                Grade = 1000;
                break;
        }


        try {
            String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
            String jdbcUser="student";
            String jdbcUserPassword="OOP2020student";
            String allCustomersQuery = "SELECT * FROM Logs where levelID="+ level + ";";
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while(resultSet.next()) {
                int globalGrade = resultSet.getInt("score");
                int globalMoves = resultSet.getInt("moves");
                //if other users have a higher grade then curr id user
                if (globalMoves <= moves && globalGrade >= Grade) {
                    if(globalGrade > yourScore){
                        higherGrades++;
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return higherGrades;
    }
    /*
     *	This method upadate the higher garde for each level
     */
    public static int[] CurrentStage(int id) {
        int []Stages = new int[12];
        try {
            String jdbcUrl="jdbc:mysql://db-mysql-ams3-67328-do-user-4468260-0.db.ondigitalocean.com:25060/oop?useUnicode=yes&characterEncoding=UTF-8&useSSL=false";
            String jdbcUser="student";
            String jdbcUserPassword="OOP2020student";
            String allCustomersQuery = "SELECT * FROM Logs where userID="+ id + ";";
            Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcUserPassword);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(allCustomersQuery);
            while(resultSet.next()) {
                int level = resultSet.getInt("levelID");
                int grade = resultSet.getInt("score");
                int moves = resultSet.getInt("moves");
                switch(level) {
                    case 0:
                        if (grade >= 125 && moves <= 290) {
                            if (grade > Stages[0]) {
                                Stages[0] = grade;
                            }
                        }
                        break;
                    case 1:
                        if (grade >= 436 && moves <= 580) {
                            if (grade > Stages[1]) {
                                Stages[1] = grade;
                            }
                        }
                        break;
                    case 3:
                        if (grade >= 713 && moves <= 580) {
                            if (grade > Stages[2]) {
                                Stages[2] = grade;
                            }
                        }
                        break;

                    case 5:
                        if (grade >= 570 && moves <= 500) {
                            if (grade > Stages[3]) {
                                Stages[3] = grade;
                            }
                        }
                        break;

                    case 9:
                        if (grade >= 480 && moves <= 580) {
                            if (grade > Stages[4]) {
                                Stages[4] = grade;
                            }
                        }
                        break;

                    case 11:
                        if (grade >= 1050 && moves <= 580) {
                            if (grade > Stages[5]) {
                                Stages[5] = grade;
                            }
                        }
                        break;

                    case 13:
                        if (grade >= 310 && moves <= 580) {
                            if (grade > Stages[6]) {
                                Stages[6] = grade;
                            }
                        }
                        break;

                    case 16:
                        if (grade >= 235 && moves <= 290) {
                            if (grade > Stages[7]) {
                                Stages[7] = grade;
                            }
                        }
                        break;

                    case 19:
                        if (grade >= 250 && moves <= 580) {
                            if (grade > Stages[8]) {
                                Stages[8] = grade;
                            }
                        }
                        break;

                    case 20:
                        if (grade >= 200 && moves <= 290) {
                            if (grade > Stages[9]) {
                                Stages[9] = grade;
                            }
                        }
                        break;

                    case 23:
                        if (grade >= 1000 && moves <= 1140) {
                            if (grade > Stages[10]) {
                                Stages[10] = grade;
                            }
                        }
                        break;
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Stages;
    }

}
