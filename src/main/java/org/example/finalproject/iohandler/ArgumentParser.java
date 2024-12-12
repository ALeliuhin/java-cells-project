package org.example.finalproject.iohandler;

import io.github.cdimascio.dotenv.Dotenv;
import org.example.finalproject.main.AdminInterface;
import org.example.finalproject.main.ClientInterface;
import org.example.finalproject.main.UserInterface;
import org.example.finalproject.main.exceptions.FileNameAlreadyExists;
import org.example.finalproject.main.exceptions.ServerUnreached;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ArgumentParser {

    private static final String LOGIN_OPT = "-l";
    private static final String OPEN_OPT = "-o";
    private static final String HELP_OPT = "--help";
    private static final String DEFAULT_FILE = "./cell_data.db";
    private static final Dotenv dotenv = Dotenv.load();

    public void parseArguments(List<String> args) throws FileNameAlreadyExists {
        if (args.isEmpty()) {
            OutputDevice.writeMessage("No arguments provided. Use <--help> for usage information.\n");
            System.exit(-1);
        }

        try {
            switch (args.get(0)) {
                case HELP_OPT:
                    displayHelp();
                    break;
//
                case LOGIN_OPT:
                    handleLoginOption(args);
                    break;

                case OPEN_OPT:
                    handleOpenOption(args);
                    break;

                default:
                    OutputDevice.writeMessage("Invalid option. Use <--help> for usage information.\n");
                    System.exit(-1);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            OutputDevice.writeMessage("Missing argument for option " + args.get(0) + "\n");
            System.exit(-1);
        } catch (IOException e) {
            throw new ServerUnreached("Server unreachable");
        }
    }

    private void displayHelp() {
        OutputDevice.writeMessage("\tINFO:\n" +
                "Options:\n" +
                "-l <user/admin> -> provides different interfaces and privileges\n" +
                "-o <path/.db> -> opens a file on a specified path; if not found, a new one is created\n" +
                "--help -> provides documentation\n\n");
    }

    private void handleLoginOption(List<String> args) throws FileNameAlreadyExists, IOException {
        if (args.size() < 2 || (!args.get(1).equals("user") && !args.get(1).equals("admin"))) {
            OutputDevice.writeMessage("Invalid or missing role for " + LOGIN_OPT + " option.\n");
            System.exit(-1);
        }

        String role = args.get(1);
        String filePath = (args.size() > 3 && args.get(2).equals(OPEN_OPT)) ? args.get(3) : DEFAULT_FILE;

        if (role.equals("admin")) {
            if (!verifyAdminPassword()) {
                OutputDevice.writeMessage("Incorrect password. Access denied.\n");
                System.exit(-1);
            }
        }

        startInterface(role.equals("user") ? new UserInterface() : new AdminInterface());
    }

    private boolean verifyAdminPassword() {
        Scanner scanner = new Scanner(System.in);
        OutputDevice.writeMessage("Enter admin password: ");
        String inputPassword = scanner.nextLine();
        String envPassword = dotenv.get("ADMIN_PASSWORD");

        return inputPassword.equals(envPassword);
    }

    private void handleOpenOption(List<String> args) throws FileNameAlreadyExists, IOException {
        if (args.size() < 4 || !args.get(2).equals(LOGIN_OPT) || (!args.get(3).equals("user") && !args.get(3).equals("admin"))) {
            OutputDevice.writeMessage("Invalid arguments for " + OPEN_OPT + " option. Use <--help> for usage information.\n");
            System.exit(-1);
        }

        String filePath = args.get(1);
        String role = args.get(3);

        if (filePath.equals(DEFAULT_FILE)) {
            throw new FileNameAlreadyExists("File name " + DEFAULT_FILE + " already exists\n");
        }

        if (role.equals("admin") && !verifyAdminPassword()) {
            OutputDevice.writeMessage("Incorrect password. Access denied.\n");
            System.exit(-1);
        }

        startInterface(role.equals("user") ? new UserInterface() : new AdminInterface());
    }

    private void startInterface(Object interfaceInstance) {
        if (interfaceInstance instanceof UserInterface) {
            ((UserInterface) interfaceInstance).start();
        } else if (interfaceInstance instanceof AdminInterface) {
            ((AdminInterface) interfaceInstance).start();
        }
    }
}