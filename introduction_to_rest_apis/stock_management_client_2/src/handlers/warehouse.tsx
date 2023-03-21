import React from "react";
import { Status } from "http";
import { type ApiMessage, type ApiSuccessMessage, type Handler, isApiErrorMessage } from "./types.ts";
import { apiErrorResponse, errorResponse, htmlResponse, redirectResponse } from "../response.tsx";
import { A, H2, H3, Button, ButtonLink, Box, Form, List, TextInput, capitalise } from "../style.tsx";

// /warehouses/:warehouseId
const warehouse: Handler = async ({ request, urlPatternResult, warehouses }) => {
  // check for warehouse
  const warehouseId = parseInt(urlPatternResult.pathname.groups.warehouseId);
  const warehouse = warehouses.find((warehouse) => warehouseId === warehouse.id);

  if (warehouse === undefined) {
    return htmlResponse(<p>Warehouse not found.</p>, Status.NotFound);
  }

  // if we have a warehouse...
  const warehouseResponse = await fetch(warehouse.links.self);
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
  return getWarehouse(warehouseResponse, warehouseMessage);
};

// /warehouses/:warehouseId - get warehouse
const getWarehouse = async (warehouseResponse: Response, warehouseMessage: ApiSuccessMessage): Promise<Response> => {
  const warehouse = warehouseMessage.data[0];
  const stockInstancesLink = typeof warehouse.links.stockInstances === "string"
    ? warehouse.links.stockInstances
    : warehouse.links.stockInstances.href
  const stockInstancesResponse = await fetch(stockInstancesLink);
  const stockInstancesMessage: ApiMessage = await stockInstancesResponse.json();

  if (isApiErrorMessage(stockInstancesMessage)) {
    return apiErrorResponse(stockInstancesMessage, stockInstancesResponse.status);
  }

  const stockInstances = stockInstancesMessage.data;

  return htmlResponse(
    <>
      <H2><A href={`/warehouses/${warehouse.id}`}>Warehouse: {warehouse.attributes.name}</A></H2>
      <div className="flex gap-3">
        <Box>
          <div className="flex justify-between items-baseline">
            <H3>Warehouse Stock</H3>
            <ButtonLink href={`/warehouses/${warehouse.id}/delivery`}>Record Stock Delivery</ButtonLink>
          </div>
          <List>
            {stockInstances.map((stockInstance) => (
              <li>
                {stockInstance.attributes.stockLine}, {stockInstance.attributes.amount}
              </li>
            ))}
          </List>
        </Box>
        <Box>
          {warehouseResponse.headers.get("Allow")?.includes("PUT") ? (
            <Form method="get">
              <div className="flex justify-between items-center">
                <H3>Edit Warehouse Details</H3>
                <Button type="submit">Submit</Button>
              </div>
              <input type="hidden" name="update" value="true" />
              {Object.entries(warehouse.attributes).map(([key, value]) => (
                <TextInput label={capitalise(key)} name={key} value={value} />
              ))}
            </Form>
          ) : null}
          {warehouseResponse.headers.get("Allow")?.includes("DELETE") ? (
            <>
              <hr />
              <Form method="get">
                <div className="flex justify-between items-center">
                  <H3>Delete Warehouse</H3>
                  <Button type="submit">Delete</Button>
                </div>
                <input type="hidden" name="delete" value="true" />
              </Form>
            </>
          ) : null}
        </Box>
      </div>
    </>
  );
};

// /warehouses/:warehouseId - update warehouse
const updateWarehouse = async (url: URL, warehouseMessage: ApiSuccessMessage): Promise<Response> => {
  const warehouse = warehouseMessage.data[0];
  const newWarehouseResponse = await fetch(warehouse.links.self, {
    method: "PUT",
    body: JSON.stringify(Object.fromEntries(url.searchParams.entries())),
    headers: { "Content-Type": "application/json" },
  });
  const newWarehouseResponseMessage: ApiMessage = await newWarehouseResponse.json();

  return isApiErrorMessage(newWarehouseResponseMessage)
    ? apiErrorResponse(newWarehouseResponseMessage, newWarehouseResponse.status)
    : getWarehouse(newWarehouseResponse, newWarehouseResponseMessage);
};

// /warehouses/:warehouseId - delete warehouse
const deleteWarehouse = async (warehouseMessage: ApiSuccessMessage): Promise<Response> => {
  const warehouse = warehouseMessage.data[0];
  const deleteWarehouseResponse = await fetch(warehouse.links.self, {
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
