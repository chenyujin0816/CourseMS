export interface JsonResult<T> {
    msg?: string;
    code?: string;
    data?: T;
}