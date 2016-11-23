package by.pvt.safronenko.library.tools.command.reader;

import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sign up page.
 */
public class ReaderSignUpPageCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ConfigurationManager.getProperty("path.page.reader.signup");
    }
}
