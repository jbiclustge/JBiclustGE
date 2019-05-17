#!/bin/bash


JAVA=$JAVA_HOME

RVERSION="3.4.3"

PASSWORD="none"
RINSTALLPATH="$HOME"
COMPILEDIR="build_dir"
COMPILEDIRPATH="none"
FILENAME=$(basename $0)
NPROC="4"
NOX11="0"
RCOMPILATIONTYPE="0"

####### change Zlib last version check http://zlib.net/
ZLIBVERSION=zlib-1.2.11

####### change bzip2 last version check http://www.bzip.org/downloads.html
BZIPVERSION=1.0.6

##### change xz utils version check http://tukaani.org/xz/
XZUTILSVERSION=xz-5.2.3

##### change libxml2 version check ftp://xmlsoft.org/libxml2/
LIBXMLVERSION=libxml2-2.9.8

##### change pcre version check ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/
PCREVERSION=pcre-8.42

##### change openssl version check https://www.openssl.org/source/
OPENSSLVERSION=openssl-1.1.1
#OPENSSLVERSION=openssl-1.0.2o

##### change curl version check https://curl.haxx.se/download/
CURLVERSION=curl-7.60.0


##### change cairo version check https://cairographics.org/news/cairo-1.14.12/
CAIROVERSION=cairo-1.14.12

##### change ncurses version check ftp://ftp.gnu.org/gnu/ncurses/
NCURSESVERSION=ncurses-6.1

##### change readline version check https://tiswww.case.edu/php/chet/readline/rltop.html
READLINEVERSION=readline-7.0


##### change GNU scientific library  version check ftp://ftp.gnu.org/gnu/gsl/
GSL=gsl-2.5

##### change libgit2  version check https://github.com/libgit2/libgit2/releases
LIBGIT=0.27.7

##### change libexpat version check https://github.com/libexpat/libexpat/releases
LIBEXPAT=expat-2.2.6
LIBEXPATRF=R_2_2_6

##### change UDUNITS-2 version check ftp://ftp.unidata.ucar.edu/pub/udunits/
LIBUDUNITS=udunits-2.2.26

##### change ICU4C version check http://site.icu-project.org/download/61#TOC-ICU4C-Download
ICU4CNAME=icu4c
ICU4CRELEASE=55
ICU4CVERSION=1
ICU4CSOURCENAME=icu

######### change R version and download url
#RDOWNLOADURL=https://cran.r-project.org/src/base-prerelease/R-latest.tar.gz
#RTAR_NAME=R-latest.tar.gz
#RVERSION=R-patched





init_setup()
{

if [ "$RVERSION" = "3.4.1" ] || [ "$RVERSION" = "3.4.3" ] || [ "$RVERSION" = "3.4.4" ] || [ "$RVERSION" = "3.5.1" ]; then
  echo "Using R version $RVERSION"
else
  RVERSION="3.4.3"
fi

RINSTALLDIR="Rlocal_$RVERSION"
RINSTALLPATH=$RINSTALLPATH/$RINSTALLDIR

if [ "$COMPILEDIRPATH" = "none" ]; then
  COMPILEDIRPATH=$RINSTALLPATH/$COMPILEDIR
fi

if [ ! -d $RINSTALLPATH ]; then 
    	 mkdir $RINSTALLPATH
fi

if [ ! -d $COMPILEDIRPATH ]; then 
    	 mkdir $COMPILEDIRPATH
fi

}


install_zlib()
{
        cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$ZLIBVERSION ]; then 
		wget http://zlib.net/$ZLIBVERSION.tar.gz
		tar xzvf $ZLIBVERSION.tar.gz
                rm $ZLIBVERSION.tar.gz
        else
                cd $COMPILEDIRPATH/$ZLIBVERSION
                make clean
        fi
	
        cd $COMPILEDIRPATH/$ZLIBVERSION
	./configure --prefix=$RINSTALLPATH
	make -j$NPROC
	make install

}

install_libxml2()
{

	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$LIBXMLVERSION ]; then
        	wget ftp://xmlsoft.org/libxml2/$LIBXMLVERSION.tar.gz
		tar xzvf $LIBXMLVERSION.tar.gz
		rm $LIBXMLVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/$LIBXMLVERSION
		make clean 
	fi
		
	cd $COMPILEDIRPATH/$LIBXMLVERSION
	./configure --prefix=$RINSTALLPATH
	make -j$NPROC
	make install
}

install_bzip2()
{
	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/bzip2-$BZIPVERSION ]; then
                #git clone https://github.com/enthought/bzip2-1.0.6.git
  		#wget http://www.bzip.org/$BZIPVERSION/bzip2-$BZIPVERSION.tar.gz
                wget --no-check-certificate https://fossies.org/linux/misc/bzip2-1.0.6.tar.gz
		tar xzvf bzip2-$BZIPVERSION.tar.gz
                rm bzip2-$BZIPVERSION.tar.gz
        else
                cd $COMPILEDIRPATH/bzip2-$BZIPVERSION
                make clean 
        fi
		
        cd $COMPILEDIRPATH/bzip2-$BZIPVERSION
	sed -i -e 's/CFLAGS=-Wall/CFLAGS=-Wall -fPIC/g' Makefile
	make -f Makefile-libbz2_so
	make clean
	make -j$NPROC
	make -n install PREFIX=$RINSTALLPATH
	make install PREFIX=$RINSTALLPATH


}

install_xzutils()
{
	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$XZUTILSVERSION ]; then
		wget --no-check-certificate http://tukaani.org/xz/$XZUTILSVERSION.tar.gz
		tar xzvf $XZUTILSVERSION.tar.gz
		rm $XZUTILSVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/$XZUTILSVERSION
		make clean
	fi
		
	cd $COMPILEDIRPATH/$XZUTILSVERSION
	./configure --prefix=$RINSTALLPATH
	make -j$NPROC
	make install


}

install_pcre()
{

	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$PCREVERSION ]; then
        	wget ftp://ftp.csx.cam.ac.uk/pub/software/programming/pcre/$PCREVERSION.tar.gz
		tar xzvf $PCREVERSION.tar.gz
		rm $PCREVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/$PCREVERSION
		make clean 
	fi
		
	cd $COMPILEDIRPATH/$PCREVERSION
	./configure --enable-utf8 --prefix=$RINSTALLPATH
	make -j$NPROC
	make install
}


install_openssl()
{
	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$OPENSSLVERSION ]; then
		wget --no-check-certificate https://www.openssl.org/source/$OPENSSLVERSION.tar.gz
		tar xzvf $OPENSSLVERSION.tar.gz
         rm $OPENSSLVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/$OPENSSLVERSION
		make clean
	fi
		
	cd $COMPILEDIRPATH/$OPENSSLVERSION
        export CFLAGS=-fPIC
	./config --prefix=$RINSTALLPATH --openssldir=$RINSTALLPATH/lib shared
    #export CFLAGS='-fPIC'
	make -j$NPROC
	make install
	

}



install_curl()
{
	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$CURLVERSION ]; then
		wget --no-check-certificate https://curl.haxx.se/download/$CURLVERSION.tar.gz
		tar xzvf $CURLVERSION.tar.gz
                rm $CURLVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/$CURLVERSION
		make clean
	fi
		
	cd $COMPILEDIRPATH/$CURLVERSION
	PKG_CONFIG_PATH=$RINSTALLPATH/lib/pkgconfig   ./configure --prefix=$RINSTALLPATH --with-ssl
	make -j$NPROC
	make install
	

}

install_cairo()
{

	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$CAIROVERSION ]; then
		wget --no-check-certificate http://cairographics.org/releases/$CAIROVERSION.tar.xz
		tar -xvf $CAIROVERSION.tar.xz
                rm $CAIROVERSION.tar.xz
	else
		cd $COMPILEDIRPATH/$CAIROVERSION
		make clean
	fi
		
	cd $COMPILEDIRPATH/$CAIROVERSION
	./configure --prefix=$RINSTALLPATH
	make -j$NPROC
	make install
	
}

install_ncurses()
{
  cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$NCURSESVERSION ]; then
		wget ftp://ftp.gnu.org/gnu/ncurses/$NCURSESVERSION.tar.gz
		tar xzvf $NCURSESVERSION.tar.gz
                rm $NCURSESVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/$NCURSESVERSION
		make clean 
	fi
        
        cd $COMPILEDIRPATH/$NCURSESVERSION
	./configure --prefix=$RINSTALLPATH --with-shared
	make -j$NPROC
	make install


}

install_readline()
{
        install_ncurses

	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$READLINEVERSION ]; then
		wget ftp://ftp.gnu.org/gnu/readline/$READLINEVERSION.tar.gz
		tar xzvf $READLINEVERSION.tar.gz
                rm $READLINEVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/$READLINEVERSION
		make clean 
	fi
		
	cd $COMPILEDIRPATH/$READLINEVERSION
	./configure --prefix=$RINSTALLPATH
	make -j$NPROC SHLIB_LIBS=$RINSTALLPATH/lib/libncurses.so
	make install
	
}

install_gsl()
{

	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$GSL ]; then
        	wget ftp://ftp.gnu.org/gnu/gsl/$GSL.tar.gz
		tar xzvf $GSL.tar.gz
		rm $GSL.tar.gz
	else
		cd $COMPILEDIRPATH/$GSL
		make clean 
	fi
		
	cd $COMPILEDIRPATH/$GSL
	./configure --prefix=$RINSTALLPATH
	make -j$NPROC
	make install
}


install_libgit2()
{

CMAKE=$(cmake --version 2>&1)

if [ $? -eq 0 ]; then
   
        cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/libgit2-$LIBGIT ]; then
        	wget --no-check-certificate https://github.com/libgit2/libgit2/archive/v$LIBGIT.tar.gz
		tar xzvf v$LIBGIT.tar.gz
		rm v$LIBGIT.tar.gz
	else
		cd $COMPILEDIRPATH/libgit2-$LIBGIT
		make clean 
	fi
		
	cd $COMPILEDIRPATH/libgit2-$LIBGIT
        mkdir build
        cd build
   
       export CFLAGS="-I$RINSTALLPATH/include" 
       export LDFLAGS="-L$RINSTALLPATH/lib"
       export PKG_CONFIG_PATH=$RINSTALLPATH/lib/pkgconfig
       cmake .. -DCMAKE_INSTALL_PREFIX=$RINSTALLPATH
       cmake --build . --target install
else
  echo "Cmake is not installed skipping installation of libgit2"

fi
   
}

install_libexpat()
{

	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$LIBEXPAT ]; then
        	wget --no-check-certificate https://github.com/libexpat/libexpat/releases/download/$LIBEXPATRF/$LIBEXPAT.tar.bz2
		tar xvjf $LIBEXPAT.tar.bz2
		rm $LIBEXPAT.tar.bz2
	else
		cd $COMPILEDIRPATH/$LIBEXPAT
		make clean 
	fi
		
	cd $COMPILEDIRPATH/$LIBEXPAT
	./configure --prefix=$RINSTALLPATH CPPFLAGS=-DXML_LARGE_SIZE
	make -j$NPROC
	make install
}

install_libudunits()
{
        install_libexpat

	cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$LIBUDUNITS ]; then
        	wget --no-check-certificate ftp://ftp.unidata.ucar.edu/pub/udunits/$LIBUDUNITS.tar.gz
		tar xzvf $LIBUDUNITS.tar.gz
		rm $LIBUDUNITS.tar.gz
	else
		cd $COMPILEDIRPATH/$LIBUDUNITS
		make clean 
	fi

        export CFLAGS="-I$RINSTALLPATH/include" 
        export LDFLAGS="-L$RINSTALLPATH/lib"
		
	cd $COMPILEDIRPATH/$LIBUDUNITS
	./configure --prefix=$RINSTALLPATH 
	make -j$NPROC
	make install
}

install_icu4c()
{
    cd $COMPILEDIRPATH
        if [ ! -d $COMPILEDIRPATH/$ICU4CSOURCENAME ]; then
		wget --no-check-certificate http://download.icu-project.org/files/icu4c/$ICU4CRELEASE.$ICU4CVERSION/$ICU4CNAME-$ICU4CRELEASE"_"$ICU4CVERSION-src.tgz
		tar zxvf $ICU4CNAME-$ICU4CRELEASE"_"$ICU4CVERSION-src.tgz
        rm $ICU4CNAME-$ICU4CRELEASE"_"$ICU4CVERSION-src.tgz
	else
		cd $COMPILEDIRPATH/$ICU4CSOURCENAME/source
		make clean 
	fi
		
	cd $COMPILEDIRPATH/$ICU4CSOURCENAME/source
	./configure --prefix=$RINSTALLPATH
	make -j$NPROC
	make install

}

download_R_version()
{

        cd $COMPILEDIRPATH
  	if [ ! -d $COMPILEDIRPATH/"R-"$RVERSION ]; then
       	 	wget --no-check-certificate https://cran.r-project.org/src/base/R-3/R-$RVERSION.tar.gz
        	tar xzvf R-$RVERSION.tar.gz
        	rm R-$RVERSION.tar.gz
	else
		cd $COMPILEDIRPATH/"R-"$RVERSION
		make clean
	fi

}


compile_R_llibs()
{


        cd $COMPILEDIRPATH/"R-"$RVERSION

        export JAVA_HOME=$JAVA
        export PATH=$RINSTALLPATH/bin:"/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin":$JAVA_HOME/bin
	export LD_LIBRARY_PATH=$RINSTALLPATH/lib:$LD_LIBRARY_PATH 
	export CFLAGS="-I$RINSTALLPATH/include" 
	export LDFLAGS="-L$RINSTALLPATH/lib"
        export PKG_CONFIG_PATH=$RINSTALLPATH/lib/pkgconfig

     if [ "$NOX11" = "1" ]; then
	   ./configure --prefix=$RINSTALLPATH  '--with-jpeglib' '--with-readline' '--with-tcltk' \
 		'--with-blas' '--with-lapack' '--enable-R-profiling' \
 		'--enable-R-shlib' '--enable-R-static-lib' '--enable-java'\
 		'--enable-memory-profiling' '--with-x=no'
     else
	   ./configure --prefix=$RINSTALLPATH '--with-cairo' \
         	'--with-jpeglib' '--with-readline' '--with-tcltk' \
 		'--with-blas' '--with-lapack' '--enable-R-profiling' \
 		'--enable-R-shlib' '--enable-R-static-lib' '--enable-java'\
 		'--enable-memory-profiling'
     fi


        make -j$NPROC
        make install

}

compile_R_slibs()
{

cd $COMPILEDIRPATH/"R-"$RVERSION


export JAVA_HOME=$JAVA
export PATH="/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin":$JAVA_HOME/bin
#export LD_LIBRARY_PATH=$RINSTALLPATH/lib:$LD_LIBRARY_PATH 


if [ "$NOX11" = "1" ]; then
	   ./configure --prefix=$RINSTALLPATH  '--with-jpeglib' '--with-readline' '--with-tcltk' \
 		'--with-blas' '--with-lapack' '--enable-R-profiling' \
 		'--enable-R-shlib' '--enable-R-static-lib' '--enable-java'\
 		'--enable-memory-profiling' '--with-x=no'
else

./configure --prefix=$RINSTALLPATH '--with-cairo' \
         	'--with-jpeglib' '--with-readline' '--with-tcltk' \
 		'--with-blas' '--with-lapack' '--enable-R-profiling' \
 		'--enable-R-shlib' '--enable-R-static-lib' '--enable-java'\
 		'--enable-memory-profiling'
fi

make -j$NPROC
make install
}


build_R_compiled_libs()
{

init_setup
install_zlib
install_bzip2
install_libxml2
install_xzutils
install_pcre
install_openssl
install_curl
if [ "$NOX11" = "0" ]; then
  install_cairo
fi
install_readline
install_gsl
#install_icu4c
install_libudunits
install_libgit2
download_R_version
compile_R_llibs
}


build_R_sys_libs()
{

init_setup
download_R_version
compile_R_slibs
}




usage()
{
    echo "usage: $FILENAME [Note:  { } represents the parameter required]
		         [-i {Path} install_dir_path ]  
                         [-j {Path} java_home (optional)] 
                         [-b {Path} build_folder (optional)]
                         [-v {R version} --> (3.4.1 | 3.4.3 = default | 3.4.4 | 3.5.1)]
                         [-n {Integer} concurrent processes in compilation (optional)]
                         [-nx compile without X11 support (optional)]
                         [-ro compile only R environment (libs need to be installed first)]
                         [-h (help)]"
}

if [ "$1" != "" ];
then

	while [ "$1" != "" ]; do
    		case $1 in
                        
		-i | --install )        shift 
                                	RINSTALLPATH=$1
                                	;;
        	
		-j | --java )           shift
                                	JAVA=$1
                                	;;
               
		-b | --build_folder )   shift
                                	COMPILEDIRPATH=$1
                                	;;

        -v | --Rversion )       shift
                                	RVERSION=$1
                                	;;
                
		-n | --nproc )          shift
                                	NPROC=$1
                                	;;
        
        -nx | --without_x )     NOX11=1
                                	;;

        -ro | --Ronly )         RCOMPILATIONTYPE=1
                                	;;

		-h | --help )           usage
                               		exit
                                	;;

       	 	 * )                    usage
                                	exit 1
    	esac
    	shift
	done

fi


echo "Java: $JAVA"
echo "RPath: $RPATH"
echo "Build Folder: $COMPILEDIRPATH"


if [ "$RCOMPILATIONTYPE" = "0" ]; then
  build_R_compiled_libs
else
  build_R_sys_libs
fi


