export interface MenuModel{
    id?: string,
    titre?:string,
    icon?:string,
    url:string,
    sousMenu?: Array<MenuModel>
}