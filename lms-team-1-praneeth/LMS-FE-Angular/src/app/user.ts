export interface User {
  id: number;
  username: string;
  email: string;
  password: string;
  profile_picture: Blob | null | any;
  role: string;
  // constructor(id: number, email: string, password: string) {
  //   this.id = id;
  //   this.email = email;
  //   this.password = password;
  // }
}

export interface login {
  userEmail: string;
  password: string;
}

export interface course {
  title: string;
  picture: string | any;
  archive: boolean;
  updatedAt: string;
  createdAt: string;
  courseDescription: string;
  livelink: string;
}

export interface editUser {
  userEmail: string;
  userName: string;
}

export interface userCourse {
  username: string;
  useremail: string;
  courseslist: clist[];
}

export interface clist {
  coursename: string;
  coursetrainer: string;
  coursecreatedate: string;
}

export interface modules {
  moduleNumber: string;
  moduleName: string;
  clinks: any;
}

export interface Video {
  videoName: string;
  videoLink: string;
}
