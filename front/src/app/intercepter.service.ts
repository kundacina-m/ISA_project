import {Inject, Injectable} from '@angular/core';
import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse, HttpResponse,
} from '@angular/common/http';
import {AuthService} from "./login/auth.service";
import {Observable, throwError} from 'rxjs';
import {map, catchError, retry} from 'rxjs/operators';



@Injectable({
  providedIn: 'root'
})
export class IntercepterService implements HttpInterceptor {
  constructor(private auth: AuthService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    var token = this.auth.getJwtToken();

    if (token != null) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });
    }
    else {
      req = req.clone({
        setHeaders: {
          'Content-Type': 'application/json'
        }
      });
    }

    return next.handle(req).pipe(
      retry(1),
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';
        if (error.error instanceof ErrorEvent) {
          // client-side error
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // server-side error
          errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }

        if(error.status == 401) {
          if(token != null) {
            this.auth.removeJwtToken();
            localStorage.removeItem('user');
          }
          errorMessage = 'Login to continue';
          window.alert(errorMessage);
        }


        return throwError(errorMessage);
      })
    );
  }
}


