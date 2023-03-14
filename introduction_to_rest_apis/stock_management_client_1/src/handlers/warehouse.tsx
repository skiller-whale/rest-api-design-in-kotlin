import React from "react";
import { Status } from "http";
import { apiBase } from "../constants.ts";
import { type ApiMessage, type ApiSuccessMessage, type Handler, type StockInstance, type Warehouse, isApiErrorMessage } from "./types.ts";
import { apiErrorResponse, errorResponse, htmlResponse, redirectResponse } from "../response.tsx";
import { A, H2, H3, Button, ButtonLink, Box, Form, List, TextInput } from "../style.tsx";

// /warehouses/:warehouseId
const warehouse: Handler = async ({ request, urlPatternResult, warehouses }) => {
  // check for warehouse
  const warehouseId = parseInt(urlPatternResult.pathname.groups.warehouseId);
  const warehouse = warehouses.find((warehouse) => warehouseId === warehouse.id);

  if (warehouse === undefined) {
    return htmlResponse(<p>Warehouse not found.</p>, Status.NotFound);
  }

  // if we have a warehouse...
  const warehouseResponse = await fetch(`${apiBase}/warehouses/${warehouseId}`);
  const warehouseMessage = await warehouseResponse.json();

  const url = new URL(request.url);

  // update warehouse
  if (url.searchParams.has("update")) {
    url.searchParams.delete("update");
    return updateWarehouse(url, warehouseMessage);
  }

  // delete warehouse
  if (url.searchParams.has("delete")) {
    url.searchParams.delete("delete");
    return deleteWarehouse(warehouseMessage);
  }

  // get warehouse
  return getWarehouse(warehouseMessage);
};

// /warehouses/:warehouseId - get warehouse
const getWarehouse = async (warehouseMessage: ApiSuccessMessage<Warehouse>): Promise<Response> => {
  const warehouse = warehouseMessage.data[0];

  // get stock instances for this warehouse
  const stockInstancesResponse = await fetch(`${apiBase}/warehouses/${warehouse.id}/stock-instances`);
  const stockInstancesMessage: ApiMessage<StockInstance> = await stockInstancesResponse.json();

  if (isApiErrorMessage(stockInstancesMessage)) {
    return apiErrorResponse(stockInstancesMessage, stockInstancesResponse.status);
  }

  const stockInstances = stockInstancesMessage.data;

  return htmlResponse(
    <>
      <H2><A href={`/warehouses/${warehouse.id}`}>Warehouse: {warehouse.name}</A></H2>
      <div className="flex gap-3">
        <Box>
          <div className="flex justify-between items-baseline">
            <H3>Warehouse Stock</H3>
            <ButtonLink href={`/warehouses/${warehouse.id}/delivery`}>Record Stock Delivery</ButtonLink>
          </div>
          <List>
            {stockInstances.map((stockInstance) => (
              <li>
                {stockInstance.stockLine.name}, {stockInstance.amount}
              </li>
            ))}
          </List>
        </Box>
        <Box>
          <Form method="get">
            <div className="flex justify-between items-center">
              <H3>Edit Warehouse Details</H3>
              <Button type="submit">Submit</Button>
            </div>
            <input type="hidden" name="update" value="true" />
            <TextInput label="Name" name="name" value={warehouse.name} />
            <TextInput label="Address" name="address" value={warehouse.address} />
          </Form>
          <hr />
          <Form method="get">
            <div className="flex justify-between items-center">
              <H3>Delete Warehouse</H3>
              <Button type="submit">Delete</Button>
            </div>
            <input type="hidden" name="delete" value="true" />
          </Form>
        </Box>
      </div>
    </>
  );
};

// /warehouses/:warehouseId - update warehouse
const updateWarehouse = async (url: URL, warehouseMessage: ApiSuccessMessage<Warehouse>): Promise<Response> => {
  const warehouse = warehouseMessage.data[0];
  const newWarehouseResponse = await fetch(`${apiBase}/warehouses/${warehouse.id}`, {
    method: "PUT",
    body: JSON.stringify(Object.fromEntries(url.searchParams.entries())),
    headers: { "Content-Type": "application/json" },
  });
  const newWarehouseResponseMessage: ApiMessage<Warehouse> = await newWarehouseResponse.json();

  return isApiErrorMessage(newWarehouseResponseMessage)
    ? apiErrorResponse(newWarehouseResponseMessage, newWarehouseResponse.status)
    : getWarehouse(newWarehouseResponseMessage);
};

// /warehouses/:warehouseId - delete warehouse
const deleteWarehouse = async (warehouseMessage: ApiSuccessMessage<Warehouse>): Promise<Response> => {
  const warehouse = warehouseMessage.data[0];
  const deleteWarehouseResponse = await fetch(`${apiBase}/warehouses/${warehouse.id}`, {
    method: "DELETE",
  });

  if (deleteWarehouseResponse.status === Status.NoContent) {
    return redirectResponse("/warehouses");
  }

  const deleteWarehouseResponseMessage = await deleteWarehouseResponse.json();
  return isApiErrorMessage(deleteWarehouseResponseMessage)
    ? apiErrorResponse(deleteWarehouseResponseMessage, deleteWarehouseResponse.status)
    : errorResponse("Unexpected response received from stock management server.");
};

export default warehouse;
