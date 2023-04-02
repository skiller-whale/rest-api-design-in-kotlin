import React from "react";
import { Status } from "http";
import { Handler, ApiMessage, Warehouse, StockLine, isApiErrorMessage } from "./handlers/types.ts";
import { apiErrorResponse, htmlResponse } from "./response.tsx";
import warehouses from "./handlers/warehouses.tsx";
import warehouse from "./handlers/warehouse.tsx";
import delivery from "./handlers/delivery.tsx";
import stocklines from "./handlers/stocklines.tsx";
import { apiBase } from "./constants.ts";

const handlers: Array<[string, Handler]> = [
  ["/", warehouses],
  ["/warehouses", warehouses],
  ["/warehouses/:warehouseId", warehouse],
  ["/warehouses/:warehouseId/delivery", delivery],
  ["/stock-lines", stocklines],
];

export default async (request: Request): Promise<Response> => {
  const warehousesResponse = await fetch(`${apiBase}/warehouses`);
  const warehousesMessage: ApiMessage<Warehouse> = await warehousesResponse.json();
  if (isApiErrorMessage(warehousesMessage)) {
    return apiErrorResponse(warehousesMessage, warehousesResponse.status)
  }

  const stockLinesResponse = await fetch(`${apiBase}/stock-lines`);
  const stockLinesMessage: ApiMessage<StockLine> = await stockLinesResponse.json();
  if (isApiErrorMessage(stockLinesMessage)) {
    return apiErrorResponse(stockLinesMessage, stockLinesResponse.status)
  }

  const warehouses = warehousesMessage.data;
  const stockLines = stockLinesMessage.data;

  const url = new URL(request.url);
  for (const [pathname, handler] of handlers) {
    const urlPattern = new URLPattern({ pathname });
    const urlPatternResult = urlPattern.exec(url);
    if (urlPatternResult) {
      return handler({ urlPatternResult, request, warehouses, stockLines });
    }
  }

  return htmlResponse(<p>Page not found.</p>, Status.NotFound);
};
