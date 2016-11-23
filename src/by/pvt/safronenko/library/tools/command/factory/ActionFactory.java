package by.pvt.safronenko.library.tools.command.factory;

import by.pvt.safronenko.library.tools.command.IndexCommand;
import by.pvt.safronenko.library.tools.command.librarian.LibrarianIndexCommand;
import by.pvt.safronenko.library.tools.command.librarian.author.LibrarianAuthorRemoveCommand;
import by.pvt.safronenko.library.tools.command.librarian.book.LibrarianBookAddCommand;
import by.pvt.safronenko.library.tools.command.librarian.book.LibrarianBookListCommand;
import by.pvt.safronenko.library.tools.command.librarian.bookcopy.LibrarianBookCopyReleaseCommand;
import by.pvt.safronenko.library.tools.command.reader.*;
import by.pvt.safronenko.library.tools.resource.MessageManager;
import by.pvt.safronenko.library.tools.command.ActionCommand;
import by.pvt.safronenko.library.tools.command.librarian.author.LibrarianAuthorAddCommand;
import by.pvt.safronenko.library.tools.command.librarian.author.LibrarianAuthorListCommand;
import by.pvt.safronenko.library.tools.command.librarian.book.LibrarianBookRemoveCommand;
import by.pvt.safronenko.library.tools.command.librarian.bookcopy.LibrarianBookCopyAddCommand;
import by.pvt.safronenko.library.tools.command.librarian.bookcopy.LibrarianBookCopyListCommand;
import by.pvt.safronenko.library.tools.command.librarian.bookcopy.LibrarianBookCopyRemoveCommand;
import by.pvt.safronenko.library.tools.command.librarian.LibrarianLoginCommand;
import by.pvt.safronenko.library.tools.command.LogoutCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * Action factory.
 */
public class ActionFactory {

    /**
     * Enumeration of all commands.
     */
    public enum CommandEnum {
        INDEX(new IndexCommand()),

        READER_SIGNUP_PAGE(new ReaderSignUpPageCommand()),
        READER_SIGNUP(new ReaderSignUpCommand()),
        READER_LOGIN(new ReaderLoginCommand()),
        READER_BOOK_SEARCH(new ReaderBookSearchCommand()),
        READER_BOOK_LEASE(new ReaderBookLeaseCommand()),
        READER_BOOK_ALL(new ReaderAllBooksCommand()),

        LIBRARIAN_LOGIN(new LibrarianLoginCommand()),
        LIBRARIAN_INDEX(new LibrarianIndexCommand()),

        LIBRARIAN_BOOK_COPY_LIST(new LibrarianBookCopyListCommand()),
        LIBRARIAN_BOOK_COPY_ADD(new LibrarianBookCopyAddCommand()),
        LIBRARIAN_BOOK_COPY_RELEASE(new LibrarianBookCopyReleaseCommand()),
        LIBRARIAN_BOOK_COPY_REMOVE(new LibrarianBookCopyRemoveCommand()),

        LIBRARIAN_BOOK_LIST(new LibrarianBookListCommand()),
        LIBRARIAN_BOOK_ADD(new LibrarianBookAddCommand()),
        LIBRARIAN_BOOK_REMOVE(new LibrarianBookRemoveCommand()),

        LIBRARIAN_AUTHOR_ADD(new LibrarianAuthorAddCommand()),
        LIBRARIAN_AUTHOR_LIST(new LibrarianAuthorListCommand()),
        LIBRARIAN_AUTHOR_REMOVE(new LibrarianAuthorRemoveCommand()),

        LOGOUT(new LogoutCommand());

        ActionCommand command;

        CommandEnum(ActionCommand command) {
            this.command = command;
        }

        public ActionCommand getCurrentCommand() {
            return command;
        }
    }

    /**
     * Factory method that provides action command by parameter in HTTP request.
     *
     * @param request Request.
     * @return Action command.
     */
    public ActionCommand defineCommand(HttpServletRequest request) {
        ActionCommand current = CommandEnum.INDEX.getCurrentCommand();

        // извлечение имени команды из запроса
        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            // если команда не задана в текущем запросе
            return current;
        }
        // получение объекта, соответствующего команде
        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
            current = currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            request.setAttribute("wrongAction", action + MessageManager.getProperty("message.wrongaction"));
        }
        return current;
    }
}