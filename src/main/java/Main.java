import javax.swing.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Set<Laptop> laptopSet;
    private static HashMap<Integer, ParamsToFilter> params;

    public static void main(String[] args) {
        laptopSet = new HashSet<>();
        fillSet(laptopSet);
        params = Laptop.getParams();
        boolean exit = false;
        while (!exit) {
            System.out.println("Возможные параметры для фильтрации: ");
            for (Map.Entry<Integer, ParamsToFilter> entry : params.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue().russianTitle());
            }
            System.out.println("Для того, чтобы отфильтровать ноутбуки введите цифру или несколько цифр,\nсоответствующих нужному" +
                    " критерию через запятую и нажмите Enter.");
            System.out.println("Для завершения работы программы введите - ESC.");
            Scanner scanner = new Scanner(System.in);
            String paramsToFilterString = scanner.nextLine();
            if (paramsToFilterString.equals("ESC")) {
                exit = true;
            }
            if (!exit) {
                boolean correctInput = true;
                List<Integer> paramsToFilterInt = new ArrayList<>();
                List<ParamsToFilter> paramsToFilter = new ArrayList<>();
                try {
                    String[] enteredParams = paramsToFilterString.split(",");
                    for (String enteredParam : enteredParams) {
                        paramsToFilterInt.add(Integer.parseInt(enteredParam));
                    }
                    for (int i = 0; i < paramsToFilterInt.size(); i++) {
                        if (params.containsKey(paramsToFilterInt.get(i)) && correctInput) {
                            paramsToFilter.add(params.get(paramsToFilterInt.get(i)));
                        } else {
                            System.out.println("Ошибка ввода. Параметра с номером " + (i + 1) + " не найдено.");
                            System.out.println("Повторите ввод");
                            correctInput = false;
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка ввода! Введите числа через запятую.");
                    correctInput = false;
                }
                if (correctInput && paramsToFilter.size() > 0) {
                    HashMap<String, String> paramValues = new HashMap<>();
                    for (ParamsToFilter param : paramsToFilter) {
                        if (param.filterType().equals(FilterType.EXACT_MATCH)) {
                            HashMap<Integer, String> answers = createAnswers(param.russianTitle());
                            System.out.println("Выберите вариант из списка для параметра : "
                                    + param.russianTitle().toUpperCase(Locale.ROOT) + "\n" +
                                    " и введите нужное число");
                            for (Map.Entry<Integer, String> entry : answers.entrySet()) {
                                System.out.println(entry.getKey() + ". " + entry.getValue());
                            }
                            int input = -1;
                            while (!answers.containsKey(input)) {
                                try {
                                    input = scanner.nextInt();
                                    if (!answers.containsKey(input)) {
                                        System.out.println("Ошибка ввода. Повторите ввод.");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Ошибка ввода. Повторите ввод.");
                                    scanner.nextLine();
                                }
                            }
                            paramValues.put(param.russianTitle(), answers.get(input));
                        } else if (param.filterType().equals(FilterType.COMPARE)) {
                            double input = -1.0;
                            System.out.println(param.russianTitle() + " больше чем : ");
                            do {
                                try {
                                    input = scanner.nextDouble();
                                } catch (InputMismatchException e) {
                                    System.out.println("Ошибка ввода. Повторите ввод.");
                                    scanner.nextLine();
                                }
                            }
                            while (input == -1.0);
                            paramValues.put(param.russianTitle(), String.valueOf(input));
                        }
                    }
                    System.out.println("Выбранные вами условия для фильтра:");
                    for (Map.Entry<String, String> entry : paramValues.entrySet()) {
                        System.out.println(entry.getKey() + " : " + entry.getValue());
                    }
                    List<Laptop> result = filterLaptops(laptopSet, paramValues);
                    System.out.println("Результат фильтрации: ");
                    if (result.size() == 0) {
                        System.out.println("Ноутбуки с заданными параметрами не найдены.");
                    }
                    for (Laptop l : result) {
                        System.out.println(l.toString());
                    }
                    System.out.println();
                }
            }
            scanner.close();
        }
    }

    private static HashMap<Integer, String> createAnswers(String russianTitle) {
        HashMap<Integer, String> map = new HashMap<>();
        switch (russianTitle) {
            case "Цвет": {
                Color[] colors = Color.values();
                for (int i = 0; i < colors.length; i++) {
                    map.put(colors[i].ordinal(), colors[i].russianName);
                }
                break;
            }
            case ("Операционная система"): {
                OperatingSystem[] operatingSystems = OperatingSystem.values();
                for (int i = 0; i < operatingSystems.length; i++) {
                    map.put(operatingSystems[i].ordinal(), operatingSystems[i].title);
                }
                break;
            }
        }
        return map;
    }


    private static List<Laptop> filterLaptops(Set<Laptop> laptopSet, HashMap<String, String> values) {
        List<Laptop> result = new ArrayList<>(laptopSet);
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (entry.getKey().equals("Цвет")) {
                result = result
                        .stream()
                        .filter(l -> l.getColor().russianName.equals(entry.getValue()))
                        .collect(Collectors.toList());
            } else if (entry.getKey().equals("Операционная система")) {
                result = result
                        .stream()
                        .filter(l -> l.getOperatingSystem().title.equals(entry.getValue()))
                        .collect(Collectors.toList());
            } else if (entry.getKey().equals("Объем жесткого диска")) {
                result = result
                        .stream()
                        .filter(l -> l.getSSD().compareTo(
                                (int) Double.parseDouble(entry.getValue())) >= 0)
                        .collect(Collectors.toList());
            } else if (entry.getKey().equals("Объем оперативной памяти")) {
                result = result
                        .stream()
                        .filter(l -> l.getRAM().compareTo(
                                (int) Double.parseDouble(entry.getValue())) >= 0)
                        .collect(Collectors.toList());
            } else if (entry.getKey().equals("Диагональ экрана")) {
                result = result
                        .stream()
                        .filter(l -> l.getScreenDiagonal().compareTo(Double.parseDouble(entry.getValue())) >= 0)
                        .collect(Collectors.toList());
            }
        }
        return result;
    }

    private static void fillSet(Set<Laptop> laptopSet) {
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_10_PR0, Color.BLACK, 512, 8, 14.1));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_11_PRO, Color.RED, 1024, 16, 17.2));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_10_PR0, Color.SILVER, 1024, 8, 15.3));
        laptopSet.add(new Laptop(OperatingSystem.ASTRA_LINUX, Color.SILVER, 512, 8, 14.1));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_10_HOME, Color.BLACK, 1024, 12, 16.4));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_11_HOME, Color.BLACK, 512, 8, 17.2));
        laptopSet.add(new Laptop(OperatingSystem.UBUNTU, Color.RED, 512, 16, 14.1));
        laptopSet.add(new Laptop(OperatingSystem.MAC_OS, Color.BLACK, 512, 8, 14.1));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_10_HOME, Color.BLACK, 1024, 16, 16.1));
        laptopSet.add(new Laptop(OperatingSystem.MAC_OS, Color.BLACK, 1024, 16, 16.1));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_11_PRO, Color.BLACK, 512, 8, 14.1));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_11_PRO, Color.RED, 1024, 16, 17.2));
        laptopSet.add(new Laptop(OperatingSystem.UBUNTU, Color.SILVER, 1024, 16, 15.3));
        laptopSet.add(new Laptop(OperatingSystem.MAC_OS, Color.SILVER, 512, 8, 14.1));
        laptopSet.add(new Laptop(OperatingSystem.MAC_OS, Color.BLACK, 1024, 12, 14.5));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_11_HOME, Color.RED, 512, 8, 17.2));
        laptopSet.add(new Laptop(OperatingSystem.UBUNTU, Color.WHITE, 512, 16, 14.1));
        laptopSet.add(new Laptop(OperatingSystem.MAC_OS, Color.WHITE, 512, 8, 16.1));
        laptopSet.add(new Laptop(OperatingSystem.WINDOWS_10_HOME, Color.SILVER, 512, 16, 15.5));
        laptopSet.add(new Laptop(OperatingSystem.MAC_OS, Color.BLUE, 1024, 16, 16.1));
    }
}
