module.exports = function (grunt) {

    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        web: "src/main/resources/static",
        asset: "<%=web%>/asset",
        less: {
            compile: {
                files: {
                    'build/css/basic.css': 'css/less/app.less'
                }
            }/*,
        yuicompress: {
            files: {
                'css/test-min.css': 'css/test.css'
            },
            options: {
                yuicompress: true
            }
        }*/
        },
        uglify: {
            options: {
                stripBanners: true,
                banner: '/*! <%= pkg.name %> - v<%= pkg.version %> - ' +
                    '<%= grunt.template.today("yyyy-mm-dd") %> Copyright www.honvay.com */',
            },
            basic: {
                files: {
                    '<%=asset%>/dist/js/basic.min.js': ['build/js/basic.jquery.js', 'build/js/basic.lib.js']
                }
            },
            module: {
                files: {
                    '<%=asset%>/dist/js/module.min.js': ['build/js/basic.angular.js', 'build/js/basic.module.js']
                }
            },
            app: {
                files: {
                    '<%=asset%>/dist/js/app.min.js': ['build/js/app.js', 'build/js/app.template.js']
                }
            },
            dashboard: {
                files: {
                    '<%=asset%>/dist/js/dashboard.min.js': ['build/js/dashboard.js', 'build/js/dashboard.template.js']
                }
            }
        },
        concat: {
            options: {
                process: function (src, filepath) {
                    console.log("concat:" + filepath);
                    return src;
                }
            },
            angular: {
                src: ['vendor/angular.js', 'vendor/angular/**/*.js', 'vendor/modules/**/*.js'],
                dest: 'build/js/basic.angular.js'
            },
            jquery: {
                src: ['vendor/jquery.min.js', 'vendor/jquery/**/*.js', 'vendor/jquery/**/*.js'],
                dest: 'build/js/basic.jquery.js'
            },
            lib: {
                src: ['vendor/libs/**/*.js', 'vendor/libs/*.js'],
                dest: 'build/js/basic.lib.js'
            },
            module: {
                src: ['<%=asset%>/src/module/*.js', '<%=asset%>/src/services/*.js', '<%=asset%>/src/directives/*.js',
                    '<%=asset%>/src/filters/*.js', '<%=asset%>/src/util.js'],
                dest: 'build/js/basic.module.js'
            },
            css: {
                src: ['css/animate.css', 'css/bootstrap.css', 'vendor/modules/**/*.css', 'vendor/angular/**/*.css', 'build/css/basic.css',
                    'css/font-awesome.min.css', 'css/iconfont.css', 'css/simple-line-icons.css'],
                dest: 'build/css/basic.css'
            },
            app: {
                src: ['<%=asset%>/src/app/app.main.js', '<%=asset%>/src/app/app.config.js', '<%=asset%>/src/app/controllers/*.js', '<%=asset%>/src/app/services/*.js'],
                dest: 'build/js/app.js'
            },
            dashboard: {
                src: ['<%=asset%>/src/dashboard/dashboard.main.js', '<%=asset%>/src/dashboard/dashboard.config.js', '<%=asset%>/src/dashboard/controllers/*.js'],
                dest: 'build/js/dashboard.js'
            }
        },
        cssmin: {
            basic: {
                options: {
                    removeDuplicateRules: true,
                    removeWhitespace: true,
                    mergeAdjacentRules: true,
                    specialComments: false
                },
                src: 'build/css/basic.css',
                dest: '<%=asset%>/dist/css/basic.min.css'
            },
            app: {
                src: 'css/app.css',
                dest: '<%=asset%>/dist/css/app.min.css'
            },
            control: {
                src: 'css/control.css',
                dest: '<%=asset%>/dist/css/control.min.css'
            },
            dashboard: {
                src: 'css/dashboard.css',
                dest: '<%=asset%>/dist/css/dashboard.min.css'
            }
        },
        htmlmin: {
            app: {
                collapseBooleanAttributes: true,
                collapseWhitespace: false,
                removeAttributeQuotes: true,
                removeComments: true,
                removeEmptyAttributes: false,
                removeRedundantAttributes: true,
                removeScriptTypeAttributes: true,
                removeStyleLinkTypeAttributes: true
            }
        },
        ngtemplates: {
            app: {
                cwd: '<%=web%>',
                src: 'asset/tpl/app/**.html',
                dest: 'build/js/app.template.js',
                options: {
                    htmlmin: '<%= htmlmin.app %>'
                }
            },
            dashboard: {
                cwd: '<%=web%>',
                src: 'asset/tpl/dashboard/**/**.html',
                dest: 'build/js/dashboard.template.js',
                options: {
                    htmlmin: '<%= htmlmin.app %>'
                }
            }
        },
        copy: {
            fonts: {
                files: [
                    // includes files within path
                    {expand: true, src: ['fonts/*'], dest: '<%=asset%>/dist'}
                ],
            },
        },
        jshint: {
            all: [
                '<%=asset%>/js/app/**/*.js'
            ],
            globals: {
                $: true,
                jQuery: true,
                angular: true
            },
            options: {
                browser: true,            // browser environment
                devel: true                //
            }
        },
        watch: {
            scripts: {
                files: ['css/*.less'],
                tasks: ['less']
            }
        }
    });

    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-angular-templates');
    grunt.loadNpmTasks('grunt-contrib-htmlmin');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-copy')
    grunt.registerTask('css', ['less', 'concat:css', 'cssmin']);
    grunt.registerTask('js', ['concat:angular', 'concat:jquery', 'concat:lib', 'concat:module', 'concat:app', 'concat:dashboard', 'uglify']);
    grunt.registerTask('basic', ['concat:angular', 'concat:jquery', 'concat:lib', 'concat:module']);
    grunt.registerTask('release', ['ngtemplates', 'js', 'css', 'copy:fonts']);
    grunt.registerTask('default', ['less', 'watch', 'concat', 'uglify', 'ngtemplates', 'jshint']);
};