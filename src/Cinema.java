import java.util.Scanner;

public class Cinema {
    private int rows;
    private int seatPerRow;
    private Seat seat = new Seat();
    private int income = 0;
    int soldTickets = 0;
    private String[][] seats;
    boolean exit = false;
    State state = State.INITIALIZE_ROWS;

    enum State {
        INITIALIZE_ROWS, INITIALIZE_SEATS_PER_ROW, MENU, SEAT_ROW_PICK, SEAT_NUMBER_PICK
    }

    public enum Tickets {
        REGULAR(10),
        DISCOUNT(8);
        int price;

        Tickets(int price) {
            this.price = price;
        }
    }

    private int getTotalSeats() {
        return this.rows * this.seatPerRow;
    }

    public static void main(String[] args) {
        Cinema cinema = new Cinema();
        Scanner scanner = new Scanner(System.in);
        while (!cinema.exit) {
            cinema.printState();
            try {
                cinema.inputInterpreter(scanner.nextInt());
            } catch (Exception ime) {
                scanner.nextLine();
                System.out.println("Wrong input!");
            }
        }
    }

    void inputInterpreter(int input) throws Exception {
        switch (this.state) {
            case INITIALIZE_ROWS:
                if (input <= 0) {
                    throw new Exception();
                }
                this.setRows(input);
                this.state = State.INITIALIZE_SEATS_PER_ROW;
                break;
            case INITIALIZE_SEATS_PER_ROW:
                if (input <= 0) {
                    throw new Exception();
                }
                this.setSeatPerRow(input);
                this.seats = new String[rows + 1][seatPerRow + 1]; //create Cinema hall with checked input
                this.generateHall();
                this.state = State.MENU;
                break;
            case SEAT_ROW_PICK:
                this.seat.setRow(input);
                this.state = State.SEAT_NUMBER_PICK;
                break;
            case SEAT_NUMBER_PICK:
                this.seat.setNumber(input);
                if (this.seat.getNumber() < 1 || this.seat.getNumber() > this.seatPerRow ||
                        this.seat.getRow() < 1 || this.seat.getRow() > this.rows) {
                    this.state = State.SEAT_ROW_PICK;
                    throw new Exception();
                }
                if (isAvailableSeat(this.seat)) {
                    this.income += getTicketPrice(this.seat);
                    this.soldTickets++;
                    this.state = State.MENU;
                } else {
                    System.out.println("That ticket has already been purchased!");
                    this.state = State.SEAT_ROW_PICK;
                }
                break;
            case MENU:
                mainMenu(input);
                break;
        }
    }

    boolean isAvailableSeat(Seat seat) {
        return seats[seat.getRow()][seat.getNumber()].equalsIgnoreCase("S") ? true : false;
    }

    public void printState() {
        switch (this.state) {
            case INITIALIZE_ROWS:
                System.out.println("Enter the number of rows:");
                break;
            case INITIALIZE_SEATS_PER_ROW:
                System.out.println("Enter the number of seats in each row:");
                break;
            case SEAT_ROW_PICK:
                System.out.println("\nEnter a row number:");
                break;
            case SEAT_NUMBER_PICK:
                System.out.println("Enter a seat number in that row:");
                break;
            case MENU:
                System.out.println("\n1. Show the seats\n" +
                        "2. Buy a ticket\n" +
                        "3. Statistics\n" +
                        "0. Exit ");
                break;
        }
    }

    public void printStatistics() {
        float percentage = (float) this.soldTickets / (this.rows * this.seatPerRow) * 100;
        System.out.printf("%nNumber of purchased tickets: %d%nPercentage: %.2f%%%nCurrent income: $%d%n",
                this.soldTickets, percentage, this.income);
        calculateProfit();

    }

    public void mainMenu(int input) {
        switch (input) {
            case 1:
                displayHall();
                break;
            case 2:
                this.state = State.SEAT_ROW_PICK;
                break;
            case 3:
                printStatistics();
                break;
            case 0:
                exit = true;
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    public void generateHall() {
        for (int i = 0; i < this.seats.length; i++) {
            for (int j = 0; j < this.seats[i].length; j++) {
                if (i == 0 && j != 0) {
                    this.seats[i][j] = String.valueOf(j);
                } else if (j == 0 && i != 0) {
                    this.seats[i][j] = String.valueOf(i);
                } else if (i != 0 && j != 0) {
                    this.seats[i][j] = "S";
                } else {
                    this.seats[i][j] = " ";
                }
            }
        }
    }

    public void displayHall() {
        System.out.println("\nCinema: ");
        for (int i = 0; i < this.seats.length; i++) {
            for (int j = 0; j < this.seats[i].length; j++) {
                System.out.printf(" ");
                System.out.print(this.seats[i][j]);
            }
            System.out.println();
        }
    }

    public void calculateProfit() {
        System.out.printf("Total income: $%d%n", this.getTotalSeats() <= 60 ? this.getTotalSeats() * Tickets.REGULAR.price :
                this.rows / 2 * this.seatPerRow * Tickets.REGULAR.price +
                        (this.rows - this.rows / 2) * this.seatPerRow * Tickets.DISCOUNT.price);
    }

    public int getTicketPrice(Seat seat) {
        int price = getTotalSeats() <= 60 ? Tickets.REGULAR.price :
                seat.row <= this.rows / 2 ? Tickets.REGULAR.price : Tickets.DISCOUNT.price;
        System.out.printf("%nTicket price: $%d%n", price);
        this.seats[seat.row][seat.number] = "B";
        return price;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }

    private void setSeatPerRow(int seatPerRow) {
        this.seatPerRow = seatPerRow;
    }

    class Seat {
        int row;
        int number;

        public void setNumber(int number) {
            this.number = number;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getNumber() {
            return number;
        }

        public int getRow() {
            return row;
        }
    }
}


