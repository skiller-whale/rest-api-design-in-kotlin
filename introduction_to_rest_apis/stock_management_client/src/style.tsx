import React, { type DetailedHTMLProps, type FC, type PropsWithChildren } from "react";

export const Page: FC<PropsWithChildren> = ({ children }) => (
  <html>
    <head>
      <title>Warehouse Stock Management Client</title>
      <meta charSet="UTF-8" />
      <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      <script src="https://cdn.tailwindcss.com?plugins=forms"></script>
    </head>
    <body className="max-w-5xl mx-auto flex flex-col gap-6 p-6">
      <div className="flex justify-between items-baseline">
        <H1>Warehouse Stock Management Client</H1>
        <div className="flex gap-3">
          <A href="/warehouses">Warehouses</A>
          <A href="/stock-lines">Stock Lines</A>
        </div>
      </div>
      {children}
    </body>
  </html>
);

export const H1: FC<PropsWithChildren> = ({ children, ...rest }) => (
  <h1 className="text-2xl font-semibold" {...rest}>
    {children}
  </h1>
);

export const H2: FC<PropsWithChildren> = ({ children, ...rest }) => (
  <h2 className="text-xl font-semibold" {...rest}>
    {children}
  </h2>
);

export const H3: FC<PropsWithChildren> = ({ children, ...rest }) => (
  <h3 className="text-lg font-semibold" {...rest}>
    {children}
  </h3>
);

export const List: FC<PropsWithChildren> = ({ children, ...rest }) => (
  <ul className="list-disc ml-6" {...rest}>
    {children}
  </ul>
);

export const A: FC<
  PropsWithChildren<
    DetailedHTMLProps<React.AnchorHTMLAttributes<HTMLAnchorElement>, HTMLAnchorElement>
  >
> = ({ children, ...rest }) => (
  <a className="text-blue-700 hover:underline" {...rest}>
    {children}
  </a>
);

export const Form: FC<
  PropsWithChildren<DetailedHTMLProps<React.FormHTMLAttributes<HTMLFormElement>, HTMLFormElement>>
> = ({ children, ...rest }) => (
  <form className="flex flex-col gap-3 mb-0" {...rest}>
    {children}
  </form>
);

export const TextInput: FC<
  { label: string } & DetailedHTMLProps<
    React.InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  >
> = ({ label, ...rest }) => (
  <div className="flex gap-3 items-baseline">
    <label className="w-40 font-semibold whitespace-nowrap">{label}</label>
    <input className="w-full" type="text" {...rest} />
  </div>
);

export const NumberInput: FC<
  { label: string } & DetailedHTMLProps<
    React.InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  >
> = ({ label, ...rest }) => (
  <div className="flex gap-3 items-baseline">
    <label className="w-40 font-semibold whitespace-nowrap">{label}</label>
    <input className="w-full" type="number" {...rest} />
  </div>
);

export const DateTimeInput: FC<
  { label: string } & DetailedHTMLProps<
    React.InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  >
> = ({ label, ...rest }) => (
  <div className="flex gap-3 items-baseline">
    <label className="w-40 font-semibold whitespace-nowrap">{label}</label>
    <input className="w-full" type="datetime-local" {...rest} />
  </div>
);

export const Select: FC<
  PropsWithChildren<
    { label: string } & DetailedHTMLProps<
      React.SelectHTMLAttributes<HTMLSelectElement>,
      HTMLSelectElement
    >
  >
> = ({ label, children, ...rest }) => (
  <div className="flex gap-3 items-baseline">
    <label className="w-40 font-semibold whitespace-nowrap">{label}</label>
    <select className="w-full" {...rest}>{children}</select>
  </div>
);

export const Button: FC<
  PropsWithChildren<
    DetailedHTMLProps<React.ButtonHTMLAttributes<HTMLButtonElement>, HTMLButtonElement>
  >
> = ({ children, ...rest }) => (
  <button className="py-2 px-3 bg-blue-700 text-white hover:bg-blue-900" {...rest}>
    {children}
  </button>
);

export const ButtonLink: FC<
  PropsWithChildren<
    DetailedHTMLProps<React.AnchorHTMLAttributes<HTMLAnchorElement>, HTMLAnchorElement>
  >
> = ({ children, ...rest }) => (
  <a className="py-2 px-3 bg-blue-700 text-white hover:bg-blue-900" {...rest}>
    {children}
  </a>
);

export const Box: FC<
  PropsWithChildren<DetailedHTMLProps<React.HTMLAttributes<HTMLDivElement>, HTMLDivElement>>
> = ({ children, ...rest }) => (
  <div className="flex-1 p-3 border border-gray-300 flex flex-col gap-3" {...rest}>
    {children}
  </div>
);

export const capitalise = (str: string): string => `${str.charAt(0).toUpperCase()}${str.slice(1)}`;
