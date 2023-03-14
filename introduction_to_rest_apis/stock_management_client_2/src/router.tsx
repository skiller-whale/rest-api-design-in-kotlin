import React from "react";
import { Status } from "http";
import { ApiMessage, Handler, isApiErrorMessage } from "./handlers/types.ts";
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
  const apiIndexResponse = await fetch(apiBase);
  const apiIndexMessage: ApiMessage = await apiIndexResponse.json();
  if (isApiErrorMessage(apiIndexMessage)) {
    return apiErrorResponse(apiIndexMessage, apiIndexResponse.status)
  }

  const apiIndex = apiIndexMessage.data[0];
  const apiLinks = apiIndex.links;
  const warehousesLink = typeof apiLinks.warehouses === "string" ? apiLinks.warehouses : apiLinks.warehouses.href;
  const stockLinesLink = typeof apiLinks.stockLines === "string" ? apiLinks.stockLines : apiLinks.stockLines.href;

  const warehousesResponse = await fetch(warehousesLink);
  const warehousesMessage: ApiMessage = await warehousesResponse.json();
  if (isApiErrorMessage(warehousesMessage)) {
    return apiErrorResponse(warehousesMessage, warehousesResponse.status)
  }

  const stockLinesResponse = await fetch(stockLinesLink);
  const stockLinesMessage: ApiMessage = await stockLinesResponse.json();
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
