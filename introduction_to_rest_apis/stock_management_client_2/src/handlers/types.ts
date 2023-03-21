export type HandlerArgs = {
  urlPatternResult: URLPatternResult;
  request: Request;
  warehouses: Representation[];
  stockLines: Representation[];
};

export type Handler = (args: HandlerArgs) => Response | Promise<Response>;

export type Representation = {
  type: string;
  id: number;
  attributes: Record<string, string>;
  links: { self: string } & Record<string, string | LinkObject>;
};

export type LinkObject = {
  href: string;
  rel?: string;
  describedby?: string;
  title?: string;
  type?: string;
};

export type ApiMessage = ApiSuccessMessage | ApiErrorMessage;

export type ApiSuccessMessage = {
  data: Representation[];
};

export type ApiErrorMessage = {
  errors: string[];
};

export const isApiErrorMessage = (message: ApiMessage): message is ApiErrorMessage =>
  "errors" in message;
