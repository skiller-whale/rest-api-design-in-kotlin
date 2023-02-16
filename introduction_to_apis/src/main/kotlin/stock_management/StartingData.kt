package stock_management

import stock_management.daos.StockInstanceDao
import stock_management.daos.StockLineDao
import stock_management.daos.WarehouseDao

val warehouses = listOf(
    Warehouse("0c249db5-3e06-4316-a931-45259fe8c733","Aldwalk"),
    Warehouse("157ddeb4-b66b-445e-8a78-1657b2fe74ab","Bedale"),
    Warehouse("249f7813-710c-483f-a787-9c602c12f067","Doncaster"),
    Warehouse("4e4a45bd-35e0-424e-b843-1d19fd141727","Goole"),
    Warehouse("8038ffda-effa-488d-830e-67505c917fa9","Hornsea"),
    Warehouse("04ac093b-4842-4a1e-9179-c2dbf1c37a7d","Ilkley"),
    Warehouse("441a23b0-cf04-4205-8888-f4c2040c36f2","Morley"),
    Warehouse("89eb475e-ec2f-421a-8023-41a3394639e3","Osset"),
    Warehouse("789b3f56-562c-4a9c-979e-fd96e92ae1f4","Pickering"),
    Warehouse("b9510fe3-0536-4937-9662-e86b104f1938","Pocklington"),
    Warehouse("39e34a65-7912-4318-9066-5a4a0f8ee1f1","Ripon"),
    Warehouse("1f790b1f-7e3d-4d2d-a440-08a220583de5","Selby"),
    Warehouse("4aee50cd-9a3d-43d3-9c46-7da5535a76c5","Skipton"),
    Warehouse("1dbb167d-0fed-4b7b-859a-b13e8441a30a","Tadcaster"),
    Warehouse("4ff0da34-13a1-4779-b2cc-3f9b54a48526","Wakefield"),
)
val stockLines = listOf(
    StockLine("36004abb-f217-4c8d-a8b6-8eff03115432","hemispherical rod end"),
    StockLine("f74ea835-48c2-45bb-878f-f5424a7b73e2","slide outlet"),
    StockLine("cddeb1fa-f58e-4e0e-a9f9-628bf6e0c5cc","nippled connector"),
    StockLine("074cce05-c1b3-490a-840d-8a546a5694b5","nippled adaptor"),
    StockLine("1cd15829-9265-4e8a-bf67-82d87b08e405","mechanical comparator"),
    StockLine("b3793acc-9681-4f7e-af4c-0fe7e0cc4f92","elbow adaptor"),
    StockLine("bffe08f8-7990-426e-8c78-9d43c9c2644f","tubing nut"),
    StockLine("e740b010-a769-4b62-8ee3-277a96d89862","grub screw"),
    StockLine("8d973584-97e6-4457-a47e-064108e1f94c","internal fan washer"),
    StockLine("59e0f8bb-b024-4d41-a9b8-1f62a1329e41","dog point"),
    StockLine("587da799-af54-4da7-a052-9484fefbb77f","half dog point"),
    StockLine("90508195-cb69-489b-93ad-54d5f2bb9dab","white metal bush"),
    StockLine("18622830-cc5b-456d-8f3f-ea4d9b046a6f","parallel stud coupling"),
    StockLine("decddb05-d7b0-4649-a70d-78a8f82deb96","back nut"),
    StockLine("9c8cf3d3-af72-4601-aaef-e676ff28871b","front nut"),
)
val stockInstances = listOf(
    StockInstance("8ed4c739-f6ee-44e8-8226-35633daa929a","b3793acc-9681-4f7e-af4c-0fe7e0cc4f92","89eb475e-ec2f-421a-8023-41a3394639e3",1),
    StockInstance("1b6cbc18-51b2-4ee5-9f34-c467f80120c4","8d973584-97e6-4457-a47e-064108e1f94c","1f790b1f-7e3d-4d2d-a440-08a220583de5",10),
    StockInstance("f2f3a3dd-2f27-4fc0-af5a-d9da01d02bbe","8d973584-97e6-4457-a47e-064108e1f94c","0c249db5-3e06-4316-a931-45259fe8c733",25),
    StockInstance("b0a41d94-478c-480a-8485-134d40f36668","b3793acc-9681-4f7e-af4c-0fe7e0cc4f92","441a23b0-cf04-4205-8888-f4c2040c36f2",50),
    StockInstance("5ba93bf7-ccaf-4648-9a07-3d71e9a18e74","e740b010-a769-4b62-8ee3-277a96d89862","1dbb167d-0fed-4b7b-859a-b13e8441a30a",100),
    StockInstance("efe378ba-3618-4bb4-a90f-31ded082ed81","36004abb-f217-4c8d-a8b6-8eff03115432","4aee50cd-9a3d-43d3-9c46-7da5535a76c5",150),
    StockInstance("dca3f02e-f366-490a-bfe2-9e6bb6fe8080","18622830-cc5b-456d-8f3f-ea4d9b046a6f","4aee50cd-9a3d-43d3-9c46-7da5535a76c5",200),
    StockInstance("ab08db07-f836-49ce-8537-d2941ccf17c5","36004abb-f217-4c8d-a8b6-8eff03115432","157ddeb4-b66b-445e-8a78-1657b2fe74ab",50),
    StockInstance("61b9f6e2-ba30-4e3d-a0f3-d6069d6259f5","cddeb1fa-f58e-4e0e-a9f9-628bf6e0c5cc","441a23b0-cf04-4205-8888-f4c2040c36f2",100),
    StockInstance("c53bc098-9a5e-4bc4-9564-ea79e3f91d9b","f74ea835-48c2-45bb-878f-f5424a7b73e2","441a23b0-cf04-4205-8888-f4c2040c36f2",150),
    StockInstance("b5d2b099-2e75-4a07-ac25-1a29b33be9d1","18622830-cc5b-456d-8f3f-ea4d9b046a6f","789b3f56-562c-4a9c-979e-fd96e92ae1f4",1),
    StockInstance("a8423bfd-13a0-4f96-9dc8-c2b34945157f","36004abb-f217-4c8d-a8b6-8eff03115432","04ac093b-4842-4a1e-9179-c2dbf1c37a7d",10),
    StockInstance("5bd0e849-7a87-4677-b652-e686b840cea3","cddeb1fa-f58e-4e0e-a9f9-628bf6e0c5cc","1f790b1f-7e3d-4d2d-a440-08a220583de5",25),
    StockInstance("de03c1de-f836-4d29-90be-6e089461303a","bffe08f8-7990-426e-8c78-9d43c9c2644f","8038ffda-effa-488d-830e-67505c917fa9",50),
    StockInstance("99047c2b-6aaa-41cc-810c-b6d97a5786f6","cddeb1fa-f58e-4e0e-a9f9-628bf6e0c5cc","0c249db5-3e06-4316-a931-45259fe8c733",100),
    StockInstance("68fd3df3-11be-482e-9fcd-efb6656b0cc8","18622830-cc5b-456d-8f3f-ea4d9b046a6f","4e4a45bd-35e0-424e-b843-1d19fd141727",150),
    StockInstance("13881d0a-83cc-49cc-a3f3-84452f466d5b","decddb05-d7b0-4649-a70d-78a8f82deb96","04ac093b-4842-4a1e-9179-c2dbf1c37a7d",200),
    StockInstance("1c87ba71-2e54-46d0-84b2-f3aefc81b146","59e0f8bb-b024-4d41-a9b8-1f62a1329e41","249f7813-710c-483f-a787-9c602c12f067",50),
    StockInstance("f0479ccd-1475-49b9-b1b8-051fbec41125","587da799-af54-4da7-a052-9484fefbb77f","4e4a45bd-35e0-424e-b843-1d19fd141727",100),
    StockInstance("7903559a-92c1-4a67-9eb1-78ef0c0aa704","074cce05-c1b3-490a-840d-8a546a5694b5","789b3f56-562c-4a9c-979e-fd96e92ae1f4",150),
    StockInstance("eb3d1fe5-ca8c-413c-9631-a1e2e94c3569","e740b010-a769-4b62-8ee3-277a96d89862","8038ffda-effa-488d-830e-67505c917fa9",1),
    StockInstance("56257ff2-7ec9-4960-ba13-1e087aabee06","36004abb-f217-4c8d-a8b6-8eff03115432","157ddeb4-b66b-445e-8a78-1657b2fe74ab",10),
    StockInstance("4a016e66-b160-41ba-9708-fdc8b20e51ae","36004abb-f217-4c8d-a8b6-8eff03115432","157ddeb4-b66b-445e-8a78-1657b2fe74ab",25),
    StockInstance("1a7891b8-7b77-490d-b115-58835f5fe481","59e0f8bb-b024-4d41-a9b8-1f62a1329e41","441a23b0-cf04-4205-8888-f4c2040c36f2",50),
    StockInstance("0595dfca-b892-4025-9ef0-00a1e1261b06","8d973584-97e6-4457-a47e-064108e1f94c","b9510fe3-0536-4937-9662-e86b104f1938",100),
    StockInstance("ec62ac75-c7fc-4f0b-8699-96a4e2683c10","b3793acc-9681-4f7e-af4c-0fe7e0cc4f92","8038ffda-effa-488d-830e-67505c917fa9",150),
    StockInstance("7179ec3c-f3b0-46cb-93d0-6776da1b715c","587da799-af54-4da7-a052-9484fefbb77f","39e34a65-7912-4318-9066-5a4a0f8ee1f1",200),
    StockInstance("4b6cef15-d0d0-4870-9d4b-66782b327a34","1cd15829-9265-4e8a-bf67-82d87b08e405","157ddeb4-b66b-445e-8a78-1657b2fe74ab",50),
    StockInstance("31546964-b80e-4a35-a605-b9a38529e433","1cd15829-9265-4e8a-bf67-82d87b08e405","789b3f56-562c-4a9c-979e-fd96e92ae1f4",100),
    StockInstance("0488319d-8528-4059-90d8-889c582c8d79","bffe08f8-7990-426e-8c78-9d43c9c2644f","157ddeb4-b66b-445e-8a78-1657b2fe74ab",150),
)

fun addStartingData(warehouseDao: WarehouseDao, stockLineDao: StockLineDao, stockInstanceDao: StockInstanceDao)
{
    for (warehouse in warehouses) {
        warehouseDao.create(warehouse)
    }

    for (stockLine in stockLines) {
        stockLineDao.create(stockLine)
    }

    for (stockInstance in stockInstances) {
        stockInstanceDao.create(stockInstance)
    }
}
