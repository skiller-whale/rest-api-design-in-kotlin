import React from "react";
import { Status } from "http";
import { type Handler } from "./types.ts";
import { htmlResponse } from "../response.tsx";
import { A, H2, H3, Button, Box, DateTimeInput, Form, NumberInput, Select, TextInput } from "../style.tsx";

// /warehouses/:warehouseId/delivery
const delivery: Handler = ({ urlPatternResult, warehouses, stockLines }) => {
  // check for warehouse
  const warehouseId = parseInt(urlPatternResult.pathname.groups.warehouseId);
  const warehouse = warehouses.find((warehouse) => warehouseId === warehouse.id);

  if (warehouse === undefined) {
    return htmlResponse(<p>Warehouse not found.</p>, Status.NotFound);
  }

  // if we have a warehouse...
  return htmlResponse(
    <>
      <div className="flex justify-between items-baseline">
        <H2><A href={`/warehouses/${warehouse.id}`}>Warehouse: {warehouse.attributes.name}</A></H2>
        <A href="/warehouses">back to warehouses</A>
      </div>
      <Box>
        <H3>Record Stock Delivery</H3>
        <Form method="get">
          <div className="flex gap-3 items-end">
            <div className="flex-1 flex flex-col gap-3">
              <Select label="From Warehouse" name="fromWarehouse">
                <option value="null">[external delivery]</option>
                {warehouses.map((warehouse) => (
                  <option value={warehouse.id}>{warehouse.attributes.name}</option>
                ))}
              </Select>
              <Select label="To Warehouse" name="toWarehouse">
                <option value="null">[external delivery]</option>
                {warehouses.map((warehouse) => (
                  <option value={warehouse.id}>{warehouse.attributes.name}</option>
                ))}
              </Select>
              <Select label="Stock Line" name="stockLine">
                {stockLines.map((stockLine) => (
                  <option value={stockLine.id}>{stockLine.attributes.name}</option>
                ))}
              </Select>
              <NumberInput label="Amount" name="amount" value="0" />
              <TextInput label="Driver" name="driver" />
              <DateTimeInput label="Departure Time" name="departure" />
              <TextInput label="Van License Plate" name="van" />
            </div>
            <div>
              <Button type="submit">Submit</Button>
            </div>
          </div>
        </Form>
      </Box>
    </>
  );
};

export default delivery;
