import com.github.javafaker.Faker;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.time.ZonedDateTime;
import org.apache.commons.codec.digest.DigestUtils;


public class DataForAuctionCDB1 {

    private static Faker faker = new Faker(new Locale("uk"));
    private static Random rand = new Random();

    private static String dgfDecisionDate(int days){
        ZonedDateTime zdt = ZonedDateTime.now();
        return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(zdt.plusDays(-days));
    }

    private static Map auctionPeriodStartDate(int accelerator){
        ZonedDateTime zdt = ZonedDateTime.now();
        String date = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ssZ").format(zdt.plusDays(2*(1440/accelerator)));

        Map <String, String> auction_period_start_date = new HashMap<>();
            auction_period_start_date.put("startDate", date);
        return auction_period_start_date;
    }

    private static Map auctionValues(){
        Random rand = new Random();
        int generated_value = rand.nextInt(10000) + 1000;
        String currency = "UAH";
        Boolean valueAddedTaxIncluded = true;

        Map value = new HashMap();
            value.put("currency", currency);
            value.put("amount", generated_value);
            value.put("valueAddedTaxIncluded", valueAddedTaxIncluded);

        Map guarantee = new HashMap();
            double guarantee_amount = generated_value * 0.05;
            guarantee.put("currency", currency);
            guarantee.put("amount", guarantee_amount);

        Map minimal_step = new HashMap();
            double step_amount = generated_value * 0.01;
            minimal_step.put("currency", currency);
            minimal_step.put("amount", step_amount);
            minimal_step.put("valueAddedTaxIncluded", valueAddedTaxIncluded);

        Map values = new HashMap<>();
            values.put("value", value);
            values.put("guarantee", guarantee);
            values.put("minimal_step", minimal_step);

        return values;
    }

    private static String generateIdForItem(){
        int number = rand.nextInt(10000) + 1;
        return DigestUtils.md5Hex("" + number);
    }

    private static ArrayList generate_items_for_auction(int number_of_items) {
        ArrayList items = new ArrayList(number_of_items);
        int count = 0;
        for(int i=0; i<number_of_items; i++){
            count += 1;

            Map item = new HashMap<>();
                item.put("description", "Предмет " + count + " " + faker.lorem().sentence(10).replace('\n', ' '));
                Map <String, String> classification = new HashMap<>();
                    classification.put("scheme", "CAV");
                    classification.put("description", "Права вимоги за кредитними договорами");
                    classification.put("id", "07000000-9");
                item.put("classification", classification);

                Map <String, String> address = new HashMap<>();
                    address.put("postalCode", "04655");
                    address.put("countryName", "Україна");
                    address.put("streetAddress", "вулиця Редьчинська, 30");
                    address.put("region", "місто Київ");
                    address.put("locality", "Київ");
                item.put("address", address);

                item.put("id", generateIdForItem());

                Map <String, String> unit = new HashMap<>();
                    unit.put("code", "E48");
                    unit.put("name", "послуга");
                item.put("unit", unit);
                item.put("quantity", rand.nextInt(1000) + 1);

            items.add(item);
        }
        return items;
    }


    private static Map generateProcuringEntity(){

        Map <String, String> contactPoint = new HashMap<>();
            contactPoint.put("telephone", "+38(000)044-45-80");
            contactPoint.put("name", "Гоголь Микола Васильович");
            contactPoint.put("email", "test@test.test");

        Map <String, String> identifier = new HashMap<>();
            identifier.put("scheme", "UA-EDR");
            identifier.put("id", "12345680");
            identifier.put("legalName", "Тестовый организатор \"Банк Ликвидатор\"");


        Map <String, String> address = new HashMap<>();
            address.put("postalCode", "00000");
            address.put("countryName", "Україна");
            address.put("streetAddress", "ул. Койкого 325");
            address.put("region", "місто Київ");
            address.put("locality", "Киев");


        Map procuringEntity = new HashMap<>();
            procuringEntity.put("contactPoint", contactPoint);
            procuringEntity.put("identifier", identifier);
            procuringEntity.put("address", address);
            procuringEntity.put("kind", "general");
            procuringEntity.put("name", "Тестовый организатор \"Банк Ликвидатор\"");

        return procuringEntity;

    }


    public static Map generate_auction_json(String procurement_method_type, int number_of_items, int accelerator, int steps, String skip_auction){
        Map auctionValues = auctionValues();

        String description = faker.lorem().sentence(20).replace('\n', ' ');
        String title = faker.lorem().sentence(10).replace('\n', ' ');

        Map data = new HashMap();
            data.put("procurementMethod", "open");
            data.put("submissionMethod", "electronicAuction");
            data.put("dgfDecisionDate", dgfDecisionDate(1));
            data.put("procurementMethodType", procurement_method_type);
            data.put("dgfDecisionID", "ID-123-456-789-0");
            data.put("description", description);
            data.put("title", title);
            data.put("tenderAttempts", rand.nextInt(8) + 1);
            data.put("auctionPeriod", auctionPeriodStartDate(accelerator));
            data.put("guarantee", auctionValues.get("guarantee"));
            data.put("status", "draft");
            data.put("procurementMethodDetails", "quick, accelerator=" + accelerator);
            data.put("title_en", "[TESTING] Title in English");
            data.put("dgfID", "N-1234567890");
            data.put("submissionMethodDetails", "quick" + skip_auction);
            data.put("items", generate_items_for_auction(number_of_items));
            data.put("value", auctionValues.get("value"));
            data.put("minimalStep", auctionValues.get("minimal_step"));
            data.put("mode", "test");
            data.put("title_ru", "[ТЕСТИРОВАНИЕ] Заголовок на русском");
            data.put("procuringEntity", generateProcuringEntity());

        // Add steps for dgfInsider
        if (procurement_method_type.equals("dgfInsider")){

            Map auctionParameters = new HashMap();
                auctionParameters.put("dutchSteps", steps);
                auctionParameters.put("type", "insider");

            data.put("auctionParameters", auctionParameters);
        }

        Map <String, Map> auction_data = new HashMap<>();
            auction_data.put("data", data);

//        JSONObject json = new JSONObject(auction_data);
//        String string = json.toString();
//        System.out.println(string);
//        JSONObject json = new JSONObject(auction_data);
//        System.out.println(json);
        return auction_data;


 }

    public static void main(String args[]){

    }
}
