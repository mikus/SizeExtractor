package net.mikus.assignment.sizeextractor.processor

import net.mikus.assignment.sizeextractor.model.Product
import spock.lang.Specification


class ProcessorSpec extends Specification {

    SizeProcessor processor = new SizeProcessor()

    private Map processName(String name) {
        Product input = new Product('123')
        input.name = name
        Product output = processor.process(input)
        [name: output.name, attributes: output.attributes.collect { [name: it.name, value: it.value] }]
    }


    def "One word size attribute should be extracted correctly"() {
        expect:
        processName(originalName) == [name: newName, attributes: attributes]

        where:
            originalName                                                                ||    newName                                                              | attributes
        'white shirt size XL'                                                           || 'white shirt'                                                           | [[name: 'size', value: 'XL']]
        'white shirt Size:XL'                                                           || 'white shirt'                                                           | [[name: 'Size', value: 'XL']]
        'Mens Diesel Jacket, Size S, Lightweight Casual Jacket, Mens Outerwear, Diesel' || 'Mens Diesel Jacket, Lightweight Casual Jacket, Mens Outerwear, Diesel' | [[name: 'Size', value: 'S']]
        'Athena S41000030P101 variator weights - Diameter: 18 x 14, Size: 9.5'          || 'Athena S41000030P101 variator weights - Diameter: 18 x 14'             | [[name: 'Size', value: '9.5']]
        'Nike Miler Women\'s Running Tank - Size: L - Colour: Mango'                    || 'Nike Miler Women\'s Running Tank - Colour: Mango'                      | [[name: 'Size', value: 'L']]
    }

    def "Compound size attribute should be extracted correctly"() {
        expect:
        processName(originalName) == [name: newName, attributes: attributes]

        where:
                                   originalName                                                                   ||    newName                                                               | attributes
        'LA Bicycle 82018 BMX Bike Frame Height 26 cm Tyre Size 20 Inches (50.8 cm) Beige'                        || 'LA Bicycle 82018 BMX Bike Frame Height 26 cm Tyre Beige'                | [[name: 'Size', value: '20 Inches (50.8 cm)']]
        'Regatta Girl\'s Lever II Waterproof Jacket - Atoll Blue, Size 11 - 12'                                   || 'Regatta Girl\'s Lever II Waterproof Jacket - Atoll Blue'                | [[name: 'Size', value: '11 - 12']]
        'Ziener Adam Jun Children\'s Ski Jacket blue Blue Atoll Size:7 years'                                     || 'Ziener Adam Jun Children\'s Ski Jacket blue Blue Atoll'                 | [[name: 'Size', value: '7 years']]
        'Ziener Gloves Kwinta GTX R Women\'s Gloves White White/Blue Atoll Size:Length 171 mm; width 178-203 mm'  || 'Ziener Gloves Kwinta GTX R Women\'s Gloves White White/Blue Atoll'      | [[name: 'Size', value: 'Length 171 mm; width 178-203 mm']]
        'F&F Active Lightweight Slip on Pumps, Women\'s, Size: Adult 06 1/2, Black'                               || 'F&F Active Lightweight Slip on Pumps, Women\'s, Black'                  | [[name: 'Size', value: 'Adult 06 1/2']]
        'Elba Ring Binder Heavyweight Pvc 2 O-ring Size 25mm A4 Blue Ref 400001508 [pack 10]'                     || 'Elba Ring Binder Heavyweight Pvc 2 O-ring Blue Ref 400001508 [pack 10]' | [[name: 'Size', value: '25mm A4']]
        'F&F Lightweight Shower Resistant Padded Jacket, Infant Girl\'s, Size: 18 - 24 months, Pink'              || 'F&F Lightweight Shower Resistant Padded Jacket, Infant Girl\'s, Pink'   | [[name: 'Size', value: '18 - 24 months']]
        'HIGH WAISTED blk linen pair of shorts //ruffled straps // sizes 6 , 8 , 10 12 ,14 , 16 and 18 years old' || 'HIGH WAISTED blk linen pair of shorts //ruffled straps'                 | [[name: 'sizes', value: '6 , 8 , 10 12 ,14 , 16 and 18 years old']]

    }

    def "Compound size keyword should be extracted correctly"() {
        expect:
        processName(originalName) == [name: newName, attributes: attributes]

        where:
                                   originalName                                                         ||    newName                                                               | attributes
        'LA Bicycle 82018 BMX Bike Frame Height 26 cm Tyre Manufacturer Size 20 Inches (50.8 cm) Beige' || 'LA Bicycle 82018 BMX Bike Frame Height 26 cm Tyre Beige'                | [[name: 'Manufacturer Size', value: '20 Inches (50.8 cm)']]
        'Rip Curl Men Shock Games Boardshort 21" Swim Shorts, Blue Atoll, Small (Manufacturer Size:30)' || 'Rip Curl Men Shock Games Boardshort 21" Swim Shorts, Blue Atoll, Small' | [[name: 'Manufacturer Size', value: '30']]
    }

    def "Multiple size attributes should be extracted correctly"() {
        expect:
        processName(originalName) == [name: newName, attributes: attributes]

        where:
                                   originalName                                                                 ||    newName                                                      | attributes
        'Isbjorn of Sweden Kid\'s Light weight Rain Jacket - Navy-Blue, Size 158/Size 164'                      || 'Isbjorn of Sweden Kid\'s Light weight Rain Jacket - Navy-Blue' | [[name: 'Size', value: '158'], [name: 'Size', value: '164']]
        'Vintage Peridot and Sterling Silver Ring, US Size 9 3/4, UK Size T, Extra Large'                       || 'Vintage Peridot and Sterling Silver Ring, Extra Large'         | [[name: 'US Size', value: '9 3/4'], [name: 'UK Size', value: 'T']]
        'BBB allen key HexT (Size: 8 mm) bike tools Multi-Coloured black / grey Size:3 mm'                      || 'BBB allen key HexT bike tools Multi-Coloured black / grey'     | [[name: 'Size', value: '8 mm'], [name: 'Size', value: '3 mm']]
        'Walther Mounts PA030D Grey Frame Size 20 x 30 cm, Picture Size 13 x 18 cm'                             || 'Walther Mounts PA030D Grey'                                    | [[name: 'Frame Size', value: '20 x 30 cm'], [name: 'Picture Size', value: '13 x 18 cm']]
        'Thermoskin Plantar Fascia Entensor Small [Men Shoe Size 5-6, Women Shoe Size 5-6.5]'                   || 'Thermoskin Plantar Fascia Entensor Small'                      | [[name: 'Men Shoe Size', value: '5-6'], [name: 'Women Shoe Size', value: '5-6.5']]
        'Tuli\'s Road Runners Premium Replacement Insoles Large (Size 10 - 12 for Ladies, Size 8 - 10 for Men)' || 'Tuli\'s Road Runners Premium Replacement Insoles Large'        | [[name: 'Size', value: '10 - 12 for Ladies'], [name: 'Size', value: '8 - 10 for Men']]
    }


    def "splitName should split text correctly"() {
        expect:
        Arrays.asList(processor.splitName(name)) == result

        where:
                                          name                                                                    || result
        'Mens Diesel Jacket, Size S, Lightweight Casual Jacket, Mens Outerwear, Diesel'                           || ['Mens Diesel Jacket', 'Size S', 'Lightweight Casual Jacket', 'Mens Outerwear', 'Diesel']
        'Athena S41000030P101 variator weights - Diameter: 18 x 14, Size: 9.5'                                    || ['Athena S41000030P101 variator weights', 'Diameter: 18 x 14', 'Size: 9.5']
        'Elba Ring Binder Heavyweight Pvc 2 O-ring Size 25mm A4 Blue Ref 400001508 [pack 10]'                     || ['Elba Ring Binder Heavyweight Pvc 2 O-ring Size 25mm A4 Blue Ref 400001508 [pack 10]']
        'F&F Lightweight Shower Resistant Padded Jacket, Infant Girl\'s, Size: 18 - 24 months, Pink'              || ['F&F Lightweight Shower Resistant Padded Jacket', 'Infant Girl\'s', 'Size: 18 - 24 months', 'Pink']
        'Regatta Girl\'s Tyson II Softshell Jacket - Atoll Blue/Lime Zest, Size 5 - 6'                            || ['Regatta Girl\'s Tyson II Softshell Jacket', 'Atoll Blue/Lime Zest', 'Size 5 - 6']
        'Nike Miler Women\'s Running Tank - Size: L - Colour: Mango'                                              || ['Nike Miler Women\'s Running Tank', 'Size: L', 'Colour: Mango']
        'HIGH WAISTED blk linen pair of shorts //ruffled straps // sizes 6 , 8 , 10 12 ,14 , 16 and 18 years old' || ['HIGH WAISTED blk linen pair of shorts', 'ruffled straps', 'sizes 6 , 8 , 10 12 ,14 , 16 and 18 years old']
    }
}
