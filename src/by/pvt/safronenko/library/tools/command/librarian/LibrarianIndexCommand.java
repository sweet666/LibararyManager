package by.pvt.safronenko.library.tools.command.librarian;

import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Librarian index page command.
 */
public class LibrarianIndexCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        return ConfigurationManager.getProperty("path.page.librarian");
    }
}
