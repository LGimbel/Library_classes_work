
import java.util.*;


public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Map<String, Map<String,Object>> Catalog = new HashMap<>();
        System.out.println("Hello world!");
        UserInterface(input,Catalog);


    }
    
    public static class branch{
        static class Patron extends branch {
            static void ViewAllAvailable(Map<String, Map<String, Object>> Catalog) {
                List<String> books = branch.librarian.BooksIn(Catalog);
                System.out.println("The books currently available are:");
                for (String title : books) {
                    System.out.println(title);
                }
            }

            static void CheckIn(Map<String, Map<String, Object>> Catalog, Scanner input) {
                boolean CHECKIN = false;
                int counter = 0;
                int currentout = branch.librarian.BooksOut(Catalog).size();
                System.out.println("Please enter either the title or isbn of the book you wish to return");
                String BookReturn = input.nextLine();
                if (BookReturn.isEmpty()) {
                    System.out.println("Return title or isbn can not be empty");
                    branch.Patron.CheckIn(Catalog, input);
                } else {
                    for (Map.Entry<String, Map<String, Object>> entry : Catalog.entrySet()) {
                        String title = entry.getKey();
                        String isbn = (String) entry.getValue().get("isbn");
                        if (BookReturn.equals(title)|| BookReturn.equals(isbn)){
                            Map<String, Object> bookInfo = entry.getValue();
                            bookInfo.put("out",CHECKIN);
                            System.out.println("Updated status of book '" + title + "' to checked in ");
                            break;

                        }
                        else{
                            counter++;
                        }
                    }
                    if (counter > currentout){
                        System.out.println("im sorry we could not find the book you searched for. \n Would you like to try again? Y/N");
                        String again = input.nextLine();
                        boolean repeat =  again.equals("y");
                        if (repeat) {
                            CheckIn(Catalog, input);
                        } else {
                            System.out.println("Have a nice day");
                        }
                    }
                }

            }
            static void CheckOut (Map<String, Map<String, Object>> Catalog, Scanner input){
                List<String> books = branch.librarian.BooksIn(Catalog);
                boolean CHECKOUT = true;
                int counter = 0;
                System.out.println("Please enter the title of the book you would like to check out");
                String toCheckOut = input.nextLine();
                if (toCheckOut.isEmpty()){
                    System.out.println("The title of the book you wish to check out must not be empty please try again");
                    branch.Patron.CheckOut(Catalog,input);
                }
                else {
                    for (Map.Entry<String, Map<String, Object>> entry : Catalog.entrySet()) {
                        String title = entry.getKey();
                        if(Catalog.containsKey(toCheckOut)){
                        if (toCheckOut.equals(title)) {
                            Map<String, Object> bookInfo = entry.getValue();
                            bookInfo.put("out", CHECKOUT);
                            break;
                        }
                    }
                        else {
                            System.out.println("It apears that the book you are trying to check out is not in our system \n would you like to try again? Y/N");
                            String again = input.nextLine();
                            boolean repeat = (again.equalsIgnoreCase("y"));
                            if (repeat){
                                branch.Patron.CheckOut(Catalog,input);
                            }
                        }
                    }
                }
            }
        }

        static class librarian extends branch{
            static void CountBookTotal(Map<String, Map<String,Object>> Catalog){
                String bookTotal = String.valueOf(Catalog.size());
                System.out.println("There are a total of "+ bookTotal+" books in the catalog." );
            }
            static List<String> BooksOut(Map<String, Map<String,Object>> Catalog){
                List<String> checkedOutBooks = new ArrayList<>();
                for (Map.Entry<String, Map<String, Object>> entry : Catalog.entrySet()) {
                    String title = entry.getKey();
                    boolean isOut = (boolean) entry.getValue().get("out");
                    if (isOut){
                        checkedOutBooks.add(title);

                    }

                }


                return checkedOutBooks;
            }
            static List<String> BooksIn(Map<String, Map<String,Object>> Catalog){
                List<String> CurOut = BooksOut(Catalog);
                List<String> CurrentlyIn = new ArrayList<>();
                for(Map.Entry<String, Map<String, Object>> entry : Catalog.entrySet()){
                    String title = entry.getKey();
                    if (!CurOut.contains(title)) {
                        CurrentlyIn.add(title);
                    }

                }
            return CurrentlyIn;
            }
            static int GetTotalBooks (Map<String, Map<String,Object>> Catalog){
                return Catalog.size();
            }
            static void GetLibraryStatus (Map<String, Map<String,Object>> Catalog) {
                System.out.println("The total Amount of books in this branch is: " + GetTotalBooks(Catalog));
                System.out.println("The books currently checked out are:");
                List<String> checkedOutBooks = BooksOut(Catalog);
                for (String title : checkedOutBooks) {
                    System.out.println(title);
                }

                System.out.println("The Books currently checked in are:");
                List<String> checkedInBooks = BooksIn(Catalog);
                for (String title : checkedInBooks) {
                    System.out.println(title);
                }
            }

            private static void AddBook(Map<String, Map<String,Object>> Catalog, Scanner input){
                Map<String,Object> info = new HashMap<>();
                //region booleans for data validation
                boolean titleStatus = false;
                boolean isbnStatus = false;
                boolean outStatusCheck = false;
                //endregion
                //region declarations
                String title = "";
                String isbn = "";
                boolean isout = false;
                //endregion
                while (!titleStatus){
                    System.out.println("please input the title of the book you would like to add: ");
                    title = input.nextLine();
                    if (title.isEmpty()){
                        System.out.println("Title cannot be empty. Please try again");
                    }
                    else{
                        titleStatus = true;
                    }
                }
                while (!isbnStatus) {
                    System.out.println("Please input the isbn of " + title + ": ");
                    isbn = input.nextLine();
                    if (isbn.isEmpty()){
                        System.out.println("Isbn can not be empty");
                    }
                    else{
                        isbnStatus = true;
                    }
                }
                while (!outStatusCheck) {
                    System.out.println("Is " + title + " currently checked out Y/N: ");
                    String isOutStr = input.nextLine();

                    if (isOutStr.isEmpty()){
                        System.out.println("Please try again");
                    } else{
                        switch (isOutStr) {
                            case "y", "Y" -> {
                                isout = true;
                                outStatusCheck = true;
                            }
                            case "n", "N" -> {
                                outStatusCheck = true;
                            }
                            default -> {
                                System.out.println("Please try again");
                            }
                        }

                    }
                }
                info.put("isbn",isbn);
                info.put("out",isout);
                Catalog.put(title,info);
                System.out.println("Would you like to add another book? Y/N");
                String again = input.nextLine();
                if (again.equals("y") || again.equals("Y")) {
                    branch.librarian.AddBook(Catalog, input);
                }
            }
       }

    }
    static  void PatronUserInterface(Scanner input,Map<String, Map<String, Object>> Catalog){
        //region string
        String UIMessage = "Welcome to the Library. What would you like to do? \n1: View all available titles. \n2: Check out a book. \n3: Check in a book. please select the chosen action by entering the corresponding number";
        //endregion
        System.out.println(UIMessage);
        String ChosenAction = input.nextLine();
        switch (ChosenAction){
            case "1":
                branch.Patron.ViewAllAvailable(Catalog);
                break;
            case "2":
                branch.Patron.CheckOut(Catalog, input);
                break;
            case "3":
                branch.Patron.CheckIn(Catalog,input);
                break;
            default:
                System.out.println("it appears that you entered, "+ ChosenAction +" which is not an available action please try again.");
                PatronUserInterface(input,Catalog);
                break;
        }
        System.out.println("Would you like to do anything else? Y/N");
        String again = input.nextLine();
        if (again.equals("y")||again.equals("Y")){
            PatronUserInterface(input,Catalog);
        }
        else {
            System.out.println("Thank you for using the library system have a nice day");

        }
    }
    static void UserInterface (Scanner input,Map<String, Map<String, Object>> Catalog){
        System.out.println("are you a patron? Y/N");
        String patronQuery = input.nextLine();
        if (patronQuery.equals("Y")||patronQuery.equals("y")){
            PatronUserInterface(input,Catalog);
        }
        else{
            System.out.println("You have selected librarian is that correct. Y/N");
            String LibrarianCorQuery = input.nextLine();
            if (LibrarianCorQuery.equals("Y") || LibrarianCorQuery.equals("y")){
                LibrarianUserInterface(input,Catalog);
            } else {
                System.out.println("Please start again.");
                UserInterface(input, Catalog);


            }
        }

    }
    static  void LibrarianUserInterface(Scanner input,Map<String, Map<String, Object>> Catalog){
        //region string
        String UIMessage = "Welcome to the Library. What would you like to do? \n1: add book. \n2: See all checked in books. \n3: Get Branch status, show all titles and whether they are in or out.\n4: See all books currently checked out. \n5: count all total books in the catalog. \nplease select the chosen action by entering the corresponding number";
        //endregion
        System.out.println(UIMessage);

        String ChosenAction = input.nextLine();
        switch (ChosenAction){
            case "1":
                branch.librarian.AddBook(Catalog,input);
                break;
            case "2":
               System.out.println(branch.librarian.BooksIn(Catalog));
                break;
            case "3":
                branch.librarian.GetLibraryStatus(Catalog);
                break;
            case "4":
                System.out.println(branch.librarian.BooksOut(Catalog));
                break;
            case "5":
                System.out.println(branch.librarian.GetTotalBooks(Catalog));
                break;

            default:
                System.out.println("it appears that you entered, "+ ChosenAction +" which is not an available action please try again.");
                LibrarianUserInterface(input,Catalog);
                break;
        }
        System.out.println("Would you like to do anything else? Y/N");
        String again = input.nextLine();
        if (again.equals("y")||again.equals("Y")){
            LibrarianUserInterface(input,Catalog);
        }
        else {
            System.out.println("Thank you for using the library system have a nice day");
            System.exit(295);

        }
    }
    }
