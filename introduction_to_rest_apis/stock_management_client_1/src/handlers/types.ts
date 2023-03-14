export type HandlerArgs = {
  urlPatternResult: URLPatternResult;
  request: Request;
  warehouses: Warehouse[];
  stockLines: StockLine[];
};

export type Handler = (args: HandlerArgs) => Response | Promise<Response>;

export type Warehouse = {
  id: number;
  name: string;
  address: string;
}

export type StockLine = {
  id: number;
  name: string;
}

export type StockInstance = {
  id: number;
  stockLine: string;
  amount: number;
}

export type ApiMessage<Representation> = ApiSuccessMessage<Representation> | ApiErrorMessage;

export type ApiSuccessMessage<Representation> = {
  data: Representation[];
};

export type ApiErrorMessage = {
  errors: string[];
};

export const isApiErrorMessage = <Representation>(
  message: ApiMessage<Representation>
): message is ApiErrorMessage => "errors" in message;
