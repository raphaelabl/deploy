export interface UserData {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber: string;
  place: string;
  houseNumber: string;
  addressAddition: string;
  zipCode: string;
  cityName: string;
  countryName: string;

  licenses?: number[];
}
