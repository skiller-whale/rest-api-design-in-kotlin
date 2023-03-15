package stock_management

import stock_management.daos.StockInstanceDao
import stock_management.daos.StockLineDao
import stock_management.daos.WarehouseDao

val warehouseNames = arrayOf(
    "Aldwalk",
    "Bedale",
    "Doncaster",
    "Goole",
    "Hornsea",
    "Ilkley",
    "Morley",
    "Osset",
    "Pickering",
    "Pocklington",
    "Ripon",
    "Selby",
    "Skipton",
    "Tadcaster",
    "Wakefield"
)

val stockLineNames = arrayOf(
    "hemispherical rod end",
    "slide outlet",
    "nippled connector",
    "nippled adaptor",
    "mechanical comparator",
    "elbow adaptor",
    "tubing nut",
    "grub screw",
    "internal fan washer",
    "dog point",
    "half dog point",
    "white metal bush",
    "parallel stud coupling",
    "back nut",
    "front nut"
)

val stockInstances = arrayOf(
    Triple(0,12,150),
    Triple(0,1,10),
    Triple(0,2,25),
    Triple(0,4,50),
    Triple(0,5,10),
    Triple(1,6,150),
    Triple(2,0,100),
    Triple(2,6,100),
    Triple(2,11,25),
    Triple(3,8,150),
    Triple(4,1,50),
    Triple(4,8,100),
    Triple(5,4,150),
    Triple(5,6,50),
    Triple(5,7,1),
    Triple(6,1,150),
    Triple(6,4,50),
    Triple(7,4,1),
    Triple(7,13,100),
    Triple(8,0,25),
    Triple(8,9,100),
    Triple(8,11,10),
    Triple(9,2,50),
    Triple(9,6,50),
    Triple(10,3,100),
    Triple(10,10,200),
    Triple(12,3,150),
    Triple(12,8,1),
    Triple(12,12,200),
    Triple(13,5,200)
)

fun addStartingData(warehouseDao: WarehouseDao, stockLineDao: StockLineDao, stockInstanceDao: StockInstanceDao)
{
    for (warehouseName in warehouseNames) {
        warehouseDao.create(warehouseName)
    }

    for (stockLineName in stockLineNames) {
        stockLineDao.create(stockLineName)
    }

    for ((warehouseId, stockLineId, itemCount) in stockInstances) {
        stockInstanceDao.create(warehouseId, stockLineId, itemCount)
    }
}
