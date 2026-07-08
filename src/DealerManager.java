import java.util.ArrayList;
import java.util.Random;

public class DealerManager {
    private ArrayList<Dealer> dealers;

    public DealerManager(ArrayList<Dealer> dealers) {
        this.dealers = dealers;
    }

    public void viewAllDealers() {
        System.out.println("\n========== ALL DEALERS ==========");

        for (Dealer dealer : dealers) {
            System.out.println(dealer);
        }
    }

    public void selectRandomFourDealers() {
        if (dealers.size() < 4) {
            System.out.println("Not enough dealers to select four.");
            return;
        }

        ArrayList<Dealer> selectedDealers = new ArrayList<>();
        Random random = new Random();

        while (selectedDealers.size() < 4) {
            int index = random.nextInt(dealers.size());
            Dealer selected = dealers.get(index);

            boolean alreadySelected = false;

            for (Dealer dealer : selectedDealers) {
                if (dealer.getDealerId().equalsIgnoreCase(selected.getDealerId())) {
                    alreadySelected = true;
                    break;
                }
            }

            if (!alreadySelected) {
                selectedDealers.add(selected);
            }
        }

        sortDealersByLocation(selectedDealers);

        System.out.println("\n========== RANDOM FOUR DEALERS ==========");

        for (Dealer dealer : selectedDealers) {
            System.out.println(dealer);
        }
    }

    private void sortDealersByLocation(ArrayList<Dealer> selectedDealers) {
        for (int i = 0; i < selectedDealers.size() - 1; i++) {
            for (int j = 0; j < selectedDealers.size() - i - 1; j++) {
                Dealer dealer1 = selectedDealers.get(j);
                Dealer dealer2 = selectedDealers.get(j + 1);

                if (dealer1.getLocation().compareToIgnoreCase(dealer2.getLocation()) > 0) {
                    Dealer temp = selectedDealers.get(j);
                    selectedDealers.set(j, selectedDealers.get(j + 1));
                    selectedDealers.set(j + 1, temp);
                }
            }
        }
    }
}